package local.kotlin.learning.dsl.models

data class Condition(
    val columnName: String = "",
    val operator: Operator = Operator.NONE,
    val value: String? = null
) {
    override fun toString(): String {
        var operatorStr = ""
        var valueStr = value

        if (value !== null) {
            operatorStr = when (operator) {
                Operator.EQ -> "="
                Operator.NON_EQ -> "!="
                else -> throw Exception("Operator not specified")
            }
        } else {
            valueStr = "null"
            operatorStr = when (operator) {
                Operator.EQ -> "is"
                Operator.NON_EQ -> "!is"
                else -> throw Exception("Operator not specified")
            }
        }

        return "$columnName $operatorStr $valueStr"
    }
}