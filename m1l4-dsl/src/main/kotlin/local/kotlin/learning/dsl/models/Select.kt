package local.kotlin.learning.dsl.models

data class Select(
    val from: String,
    val columns: List<Column>,
    val where: String
) {
    override fun toString(): String {
        var columnsStr = "*";

        if (!columns.isEmpty()) {
            columnsStr = columns.joinToString(", ") { it.toString() }
        }

        var sql = "select $columnsStr from $from"

        if (!where.isEmpty()) {
            sql += " where $where"
        }

        return sql
    }
}