package local.learning.app.biz.workers.expense

import com.crowdproj.kotlin.cor.handlers.CorChainDsl
import com.crowdproj.kotlin.cor.handlers.chain
import com.crowdproj.kotlin.cor.handlers.worker
import local.learning.app.biz.groups.expense.ExpenseAccessDsl
import local.learning.common.ExpenseContext
import local.learning.common.errors.ErrorCode
import local.learning.common.errors.ErrorGroup
import local.learning.common.helpers.addError
import local.learning.common.helpers.resolveRelationsTo
import local.learning.common.models.Error
import local.learning.common.models.State
import local.learning.common.models.access.Role
import local.learning.common.models.expense.ExpenseCommand
import local.learning.common.models.expense.ExpensePermission
import local.learning.common.models.expense.ExpenseRelation

@ExpenseAccessDsl
fun CorChainDsl<ExpenseContext>.collectPermissions(title: String) = worker {
    this.title = title
    on { state == State.RUNNING }
    handle {
        if (!rolePermissions.containsKey(principal.role)) {
            addError(
                Error(
                    ErrorCode.ACCESS_DENY,
                    ErrorGroup.ACCESS,
                    "",
                    "Empty permissions for role ${principal.role}"
                )
            )
            state = State.FAILING
        } else {
            rolePermissions[principal.role]?.let { permissions.addAll(it) }
        }
    }
}

@ExpenseAccessDsl
fun CorChainDsl<ExpenseContext>.validateAccess(title: String) = chain {
    this.title = title
    on { state == State.RUNNING }
    worker("Вычисление отношения карты к принципалу") {
        expenseRepoResult.principalRelations = expenseRepoResult.resolveRelationsTo(principal)
    }
    worker("Вычисление доступа к карте") {
        permitted = checkPermitted(command, expenseRepoResult.principalRelations, permissions)
    }
    worker {
        this.title = "Валидация прав доступа"
        description = "Проверка наличия прав для выполнения операции"
        on { !permitted }
        handle {
            addError(
                Error(
                    ErrorCode.ACCESS_DENY,
                    ErrorGroup.ACCESS,
                    "",
                    "User is not allowed to perform this operation"
                )
            )
            state = State.FAILING
        }
    }
}

private fun checkPermitted(
    command: ExpenseCommand,
    relations: Set<ExpenseRelation>,
    permissions: Set<ExpensePermission>
): Boolean {
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
    val command: ExpenseCommand,
    val permission: ExpensePermission,
    val relation: ExpenseRelation
)

private val accessTable = mapOf(
    // Create
    AccessTableConditions(
        command = ExpenseCommand.CREATE,
        permission = ExpensePermission.CREATE_OWN,
        relation = ExpenseRelation.NEW
    ) to true,

    // Read
    AccessTableConditions(
        command = ExpenseCommand.READ,
        permission = ExpensePermission.READ_OWN,
        relation = ExpenseRelation.OWN,
    ) to true,

    // Update
    AccessTableConditions(
        command = ExpenseCommand.UPDATE,
        permission = ExpensePermission.UPDATE_OWN,
        relation = ExpenseRelation.OWN,
    ) to true,

    // Delete
    AccessTableConditions(
        command = ExpenseCommand.DELETE,
        permission = ExpensePermission.DELETE_OWN,
        relation = ExpenseRelation.OWN,
    ) to true,

    // Search
    AccessTableConditions(
        command = ExpenseCommand.SEARCH,
        permission = ExpensePermission.SEARCH_OWN,
        relation = ExpenseRelation.OWN,
    ) to true,

    // Stats
    AccessTableConditions(
        command = ExpenseCommand.STATS,
        permission = ExpensePermission.STATISTIC_OWN,
        relation = ExpenseRelation.OWN,
    ) to true,
)

private val rolePermissions = mapOf(
    Role.USER to setOf(
        ExpensePermission.CREATE_OWN,
        ExpensePermission.READ_OWN,
        ExpensePermission.UPDATE_OWN,
        ExpensePermission.DELETE_OWN,
        ExpensePermission.SEARCH_OWN,
        ExpensePermission.STATISTIC_OWN
    )
)