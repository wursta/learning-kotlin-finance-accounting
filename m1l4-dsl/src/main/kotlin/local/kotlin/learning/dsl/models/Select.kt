package local.kotlin.learning.dsl.models

data class Select(
    val from: String,
    val columns: List<Column>,
    val whereBlocks: List<Where>
) {
    override fun toString(): String {
        var columnsStr = "*"

        if (!columns.isEmpty()) {
            columnsStr = columns.joinToString(", ") { it.toString() }
        }

        var sql = "select $columnsStr from $from"


        if (!whereBlocks.isEmpty()) {
            sql += " where " + whereBlocks.joinToString(separator = " and ") { it.toString() }
        }

        return sql
    }
}