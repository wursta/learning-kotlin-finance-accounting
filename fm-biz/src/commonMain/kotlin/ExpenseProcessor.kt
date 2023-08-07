package local.learning.app.biz

import com.crowdproj.kotlin.cor.handlers.worker
import com.crowdproj.kotlin.cor.rootChain
import kotlinx.datetime.Clock
import local.learning.app.biz.exception.UnexpectedContext
import local.learning.app.biz.groups.expense.*
import local.learning.app.biz.workers.expense.*
import local.learning.common.CorSettings
import local.learning.common.ExpenseContext
import local.learning.common.IContext
import local.learning.common.errors.ErrorCode
import local.learning.common.errors.ErrorGroup
import local.learning.common.exceptions.InvalidFieldFormat
import local.learning.common.helpers.addError
import local.learning.common.log.ILogWrapper
import local.learning.common.models.Error
import local.learning.common.models.State
import local.learning.common.models.expense.ExpenseCommand
import local.learning.common.models.expense.ExpenseGuid

class ExpenseProcessor(val corSettings: CorSettings = CorSettings.NONE): IProcessor {
    override suspend fun exec(ctx: IContext) {
        if (ctx !is ExpenseContext) {
            throw UnexpectedContext()
        }

        return BusinessChain.exec(ctx.apply{
            this.corSettings = this@ExpenseProcessor.corSettings
        })
    }

    suspend fun <T> process(
        logger: ILogWrapper,
        logId: String,
        command: ExpenseCommand,
        fromTransport: suspend (ExpenseContext) -> Unit,
        sendResponse: suspend (ExpenseContext) -> T
    ): T {
        val ctx = ExpenseContext(
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

            operation("Создание новой траты", ExpenseCommand.CREATE) {
                stubCreate("Обработка стабов создания")

                validation {
                    worker("Копируем поля в expenseValidating") { expenseValidating = expenseRequest.copy() }
                    worker("Очистка guid") { expenseValidating.guid = ExpenseGuid.NONE }

                    validateAmount("Проверка суммы")
                    validateCardGuid("Проверка формата guid карты")
                    validateCategoryGuid("Проверка формата guid категории")
                }

                access {
                    collectPermissions("Вычисление разрешений для пользователя")
                    validateAccess("Проверка прав доступа")
                }

                repoCreate()
                prepareExpenseResult()
            }

            operation("Чтение траты", ExpenseCommand.READ) {
                stubRead("Обработка стабов чтения")

                validation {
                    worker("Копируем поля в expenseValidating") { expenseValidating = expenseRequest.copy() }

                    validateGuid("Проверка guid траты")
                }

                access {
                    collectPermissions("Вычисление разрешений для пользователя")
                    repoRead()
                    validateAccess("Проверка прав доступа")
                }

                prepareExpenseResult()
            }

            operation("Обновление траты", ExpenseCommand.UPDATE) {
                stubUpdate("Обработка стабов обновления")

                validation {
                    worker("Копируем поля в expenseValidating") { expenseValidating = expenseRequest.copy() }

                    validateGuid("Проверка guid траты")
                    validateAmount("Проверка суммы")
                    validateCardGuid("Проверка формата guid карты")
                    validateCategoryGuid("Проверка формата guid категории")
                }

                access {
                    collectPermissions("Вычисление разрешений для пользователя")
                    repoRead()
                    validateAccess("Проверка прав доступа")
                }

                repoUpdate()
                prepareExpenseResult()
            }

            operation("Удаление траты", ExpenseCommand.DELETE) {
                stubDelete("Обработка стабов удаления")

                validation {
                    worker("Копируем поля в expenseValidating") { expenseValidating = expenseRequest.copy() }

                    validateGuid("Проверка guid траты")
                }

                access {
                    collectPermissions("Вычисление разрешений для пользователя")
                    repoRead()
                    validateAccess("Проверка прав доступа")
                }

                repoDelete()
                prepareExpenseResult()
            }

            operation("Поиск трат", ExpenseCommand.SEARCH) {
                stubSearch("Обработка стабов поиска")

                validation {
                    worker("Копируем поля в expenseSearchValidating") { expenseSearchValidating = expenseSearchRequest.copy() }

                    validateSearchFilterAmountFrom("Проверка фильтра поиска amount_from")
                    validateSearchFilterAmountTo("Проверка фильтра поиска amount_to")
                    validateSearchFilterDateFrom("Проверка фильтра поиска date_from")
                    validateSearchFilterDateTo("Проверка фильтра поиска date_to")
                    validateSearchFilterSources("Проверка фильтра поиска sources")
                }

                access {
                    collectPermissions("Вычисление разрешений для пользователя")
                }

                repoSearch()
                prepareSearchResult()
            }

            operation("Статистика трат", ExpenseCommand.STATS) {
                stubStatistic("Обработка стабов статистики")

                validation {
                    worker("Копируем поля в expenseStatisticValidating") { expenseStatisticValidating = expenseStatisticRequest.copy() }
                    validateStatisticFilterDateFrom("Проверка фильтра стратистики date_from")
                    validateStatisticFilterDateTo("Проверка фильтра стратистики date_to")
                }

                access {
                    collectPermissions("Вычисление разрешений для пользователя")
                }

                repoStatistic()
                prepareStatisticResult()
            }
        }.build()
    }
}