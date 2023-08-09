package local.learning.app.biz.workers.card

import com.crowdproj.kotlin.cor.handlers.CorChainDsl
import com.crowdproj.kotlin.cor.handlers.worker
import local.learning.common.CardContext
import local.learning.common.helpers.repoFail
import local.learning.common.models.State
import local.learning.common.models.WorkMode
import local.learning.common.repo.ICardRepository
import local.learning.common.repo.card.DbCardGuidRequest
import local.learning.common.repo.card.DbCardRequest

fun CorChainDsl<CardContext>.initRepo() = worker {
    this.title = "Вычисление основного рабочего репозитория в зависимости от запрошенного режима работы"
    on { state == State.RUNNING }
    handle {
        if (workMode == WorkMode.TEST) {
            repo = corSettings.cardRepoTest
        } else if (workMode == WorkMode.PROD) {
            repo = corSettings.cardRepoProd
        }
        if (workMode != WorkMode.STUB && repo == ICardRepository.NONE) {
            throw Exception("The database is unconfigured for chosen work mode [$workMode]")
        }
    }
}
fun CorChainDsl<CardContext>.repoCreate() = worker {
    this.title = "Добавление новой карты"
    on { state == State.RUNNING }
    handle {
        val request = DbCardRequest(cardValidating.copy(
            createdBy = principal.id
        ))
        val result = repo.create(request)
        val resultCard = result.card
        if (result.success && resultCard != null) {
            cardRepoResult = resultCard
        } else {
            repoFail(result.errors)
        }
    }
}

fun CorChainDsl<CardContext>.repoRead() = worker {
    this.title = "Получение данных карты"
    on { state == State.RUNNING }
    handle {
        val request = DbCardGuidRequest(guid = cardValidating.guid, lockGuid = cardValidating.lockGuid)
        val result = repo.read(request)
        val resultCard = result.card
        if (result.success && resultCard != null) {
            cardRepoResult = resultCard
        } else {
            repoFail(result.errors)
        }
    }
}

fun CorChainDsl<CardContext>.repoUpdate() = worker {
    this.title = "Обновление данных карты"
    on { state == State.RUNNING }
    handle {
        val request = DbCardRequest(cardValidating.copy())
        val result = repo.update(request)
        val resultCard = result.card
        if (result.success && resultCard != null) {
            cardRepoResult = resultCard
        } else {
            repoFail(result.errors)
        }
    }
}

fun CorChainDsl<CardContext>.repoDelete() = worker {
    this.title = "Удаление карты"
    on { state == State.RUNNING }
    handle {
        val request = DbCardGuidRequest(guid = cardValidating.guid, lockGuid = cardValidating.lockGuid)
        val result = repo.delete(request)
        val resultCard = result.card
        if (result.success && resultCard != null) {
            cardRepoResult = resultCard
        } else {
            repoFail(result.errors)
        }
    }
}