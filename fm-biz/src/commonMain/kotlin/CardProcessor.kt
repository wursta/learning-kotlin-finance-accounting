package local.learning.app.biz

import com.crowdproj.kotlin.cor.handlers.worker
import com.crowdproj.kotlin.cor.rootChain
import kotlinx.datetime.Clock
import local.learning.app.biz.exception.UnexpectedContext
import local.learning.app.biz.groups.card.*
import local.learning.app.biz.workers.card.*
import local.learning.common.CardContext
import local.learning.common.CorSettings
import local.learning.common.IContext
import local.learning.common.errors.ErrorCode
import local.learning.common.errors.ErrorGroup
import local.learning.common.exceptions.InvalidFieldFormat
import local.learning.common.helpers.addError
import local.learning.common.log.ILogWrapper
import local.learning.common.models.Error
import local.learning.common.models.State
import local.learning.common.models.card.CardCommand
import local.learning.common.models.card.CardGuid

class CardProcessor(val corSettings: CorSettings = CorSettings.NONE) : IProcessor {
    override suspend fun exec(ctx: IContext) {
        if (ctx !is CardContext) {
            throw UnexpectedContext()
        }

        return BusinessChain.exec(ctx.apply {
            this.corSettings = this@CardProcessor.corSettings
        })
    }

    suspend fun <T> process(
        logger: ILogWrapper,
        logId: String,
        command: CardCommand,
        fromTransport: suspend (CardContext) -> Unit,
        sendResponse: suspend (CardContext) -> T
    ): T {
        val ctx = CardContext(
            timeStart = Clock.System.now()
        )
        var realCommand = command

        return try {
            logger.doWithLogging {
                fromTransport(ctx)
                realCommand = ctx.command

                logger.info(
                    msg = "$realCommand request received",
                    //data = ctx.toLog("${logId}-got")
                )

                exec(ctx)

                logger.info(
                    msg = "$realCommand request processed",
                    //data = ctx.toLog("${logId}-handled")
                )

                sendResponse(ctx)
            }
        } catch (e: InvalidFieldFormat) {
            ctx.state = State.FAILING
            ctx.addError(Error(ErrorCode.INVALID_FIELD_FORMAT, ErrorGroup.VALIDATION, e.field, e.message ?: ""))
            sendResponse(ctx)
        } catch (e: Throwable) {
            logger.doWithLogging(id = "${logId}-failure") {
                logger.error(
                    msg = "$realCommand process failed"
                    //data = ctx.toLog("${logId}-got")
                )

                ctx.command = realCommand
                ctx.state = State.FAILING
                ctx.addError(Error(ErrorCode.UNKNOWN, ErrorGroup.EXCEPTIONS, "", e.message ?: ""))
                sendResponse(ctx)
            }
        }
    }

    companion object {
        private val BusinessChain = rootChain {
            init("Инициализация")
            initRepo()

            operation("Создание новой карты", CardCommand.CREATE) {
                stubCreate("Обработка стабов создания")

                validation {
                    worker("Копируем поля в cardValidating") { cardValidating = cardRequest.copy() }
                    worker("Очистка guid") { cardValidating.guid = CardGuid.NONE }
                    worker("Очистка owner") { cardValidating.owner = cardValidating.owner.trim() }
                    validateNumberFormat("Проверка формата номера карты")
                    validateValidFor("Проверка формата срока действия карты")
                    validateOwner("Проверка формата владельца")
                    validateBankGuid("Проверка формата guid банка")
                }


                repoCreate()
                prepareResponse()
            }

            operation("Чтение карты", CardCommand.READ) {
                stubRead("Обработка стабов чтения")

                validation {
                    worker("Копируем поля в cardValidating") { cardValidating = cardRequest.copy() }
                    validateGuid("Проверка формата guid карты")
                }

                repoRead()
                prepareResponse()
            }

            operation("Обновление карты", CardCommand.UPDATE) {
                stubUpdate("Обработка стабов обновления")

                validation {
                    worker("Копируем поля в cardValidating") { cardValidating = cardRequest.copy() }
                    worker("Очистка owner") { cardValidating.owner = cardValidating.owner.trim() }
                    validateGuid("Проверка формата guid карты")
                    validateNumberFormat("Проверка формата номера карты")
                    validateValidFor("Проверка формата срока действия карты")
                    validateOwner("Проверка формата владельца")
                    validateBankGuid("Проверка формата guid банка")
                }

                repoUpdate()
                prepareResponse()
            }

            operation("Удаление карты", CardCommand.DELETE) {
                stubDelete("Обработка стабов удаления")

                validation {
                    worker("Копируем поля в cardValidating") { cardValidating = cardRequest.copy() }
                    validateGuid("Проверка формата guid карты")
                }

                repoDelete()
                prepareResponse()
            }
        }.build()
    }
}