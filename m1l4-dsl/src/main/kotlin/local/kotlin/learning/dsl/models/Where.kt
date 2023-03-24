package local.kotlin.learning.dsl.models
data class Where (
    val logic: ConditionsLogic = ConditionsLogic.AND,
    val conditions: List<Condition> = emptyList()
) {
    override fun toString(): String {
        val logicStr = when (logic) {
            ConditionsLogic.AND -> "and"
            ConditionsLogic.OR -> "or"
        }
        var conditionStr = conditions.joinToString(" $logicStr ") { it.toString() }
        if (conditions.count() > 1) {
            conditionStr = "($conditionStr)"
        }
        return conditionStr
    }
}