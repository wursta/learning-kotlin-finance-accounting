package local.learning.app.biz.workers.card

import com.crowdproj.kotlin.cor.handlers.CorChainDsl
import com.crowdproj.kotlin.cor.handlers.chain
import com.crowdproj.kotlin.cor.handlers.worker
import local.learning.app.biz.groups.card.CardAccessDsl
import local.learning.common.CardContext
import local.learning.common.errors.ErrorCode
import local.learning.common.errors.ErrorGroup
import local.learning.common.helpers.addError
import local.learning.common.helpers.resolveRelationsTo
import local.learning.common.models.Error
import local.learning.common.models.State
import local.learning.common.models.access.Role
import local.learning.common.models.card.CardCommand
import local.learning.common.models.card.CardPermission
import local.learning.common.models.card.CardRelation

@CardAccessDsl
fun CorChainDsl<CardContext>.collectPermissions(title: String) = worker {
    this.title = title
    on { state == State.RUNNING  }
    handle {
        if (!rolePermissions.containsKey(principal.role)) {
            addError(Error(
                ErrorCode.ACCESS_DENY,
                ErrorGroup.ACCESS,
                "",
                "Empty permissions for role ${principal.role}"
            ))
            state = State.FAILING
        } else {
            rolePermissions[principal.role]?.let { permissions.addAll(it) }
        }
    }
}

@CardAccessDsl
fun CorChainDsl<CardContext>.validateAccess(title: String) = chain {
    this.title = title
    on { state == State.RUNNING  }
    worker("Вычисление отношения карты к принципалу") {
        cardRepoResult.principalRelations = cardRepoResult.resolveRelationsTo(principal)
    }
    worker("Вычисление доступа к карте") {
        permitted = checkPermitted(command, cardRepoResult.principalRelations, permissions)
    }
    worker {
        this.title = "Валидация прав доступа"
        description = "Проверка наличия прав для выполнения операции"
        on { !permitted }
        handle {
            addError(Error(
                ErrorCode.ACCESS_DENY,
                ErrorGroup.ACCESS,
                "",
                "User is not allowed to perform this operation"
            ))
            state = State.FAILING
        }
    }
}

private fun checkPermitted(command: CardCommand, relations: Set<CardRelation>, permissions: Set<CardPermission>): Boolean
{
    return relations.asSequence().flatMap { relation ->
        permissions.map { permission ->
            AccessTableConditions(
                command = command,
                permission = permission,
                relation = relation,
            )
        }
    }.any {
        accessTable[it] != null
    }
}

private data class AccessTableConditions(
    val command: CardCommand,
    val permission: CardPermission,
    val relation: CardRelation
)

private val accessTable = mapOf(
    // Create
    AccessTableConditions(
        command = CardCommand.CREATE,
        permission = CardPermission.CREATE_OWN,
        relation = CardRelation.NEW
    ) to true,

    // Read
    AccessTableConditions(
        command = CardCommand.READ,
        permission = CardPermission.READ_OWN,
        relation = CardRelation.OWN,
    ) to true,

    // Update
    AccessTableConditions(
        command = CardCommand.UPDATE,
        permission = CardPermission.UPDATE_OWN,
        relation = CardRelation.OWN,
    ) to true,

    // Delete
    AccessTableConditions(
        command = CardCommand.DELETE,
        permission = CardPermission.DELETE_OWN,
        relation = CardRelation.OWN,
    ) to true,
)

private val rolePermissions = mapOf(
    Role.USER to setOf(
        CardPermission.CREATE_OWN,
        CardPermission.READ_OWN,
        CardPermission.UPDATE_OWN,
        CardPermission.DELETE_OWN,
    )
)