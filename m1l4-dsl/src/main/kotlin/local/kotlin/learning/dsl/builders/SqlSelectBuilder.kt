package local.kotlin.learning.dsl.builders

import local.kotlin.learning.dsl.models.*

@DslMarker
annotation class QueryDsl

@DslMarker
annotation class QueryFromDsl

@DslMarker
annotation class QuerySelectDsl

@QueryDsl
fun query(block: SqlSelectBuilder.() -> Unit) = SqlSelectBuilder().apply(block)

@QueryDsl
class SqlSelectBuilder {
    private var tableName: String = ""
    private var columns: MutableList<Column> = mutableListOf()
    private var where: SqlWhereBuilder = SqlWhereBuilder()

    @QueryFromDsl
    fun from(table: String) {
        tableName = table
    }

    @QuerySelectDsl
    fun select(vararg columnNames: String) {
        for (column in columnNames) {
            columns.add(Column(column))
        }
    }

    @QueryWhereDsl
    fun where(block: SqlWhereBuilder.() -> Unit) {
        where.apply(block)
    }

    fun or(block: SqlWhereBuilder.() -> Unit) {
        where.setLogic(ConditionsLogic.OR).apply(block)
    }

    fun build(): String {
        if (tableName.isEmpty()) {
            throw Exception("Не установлена таблица")
        }

        val whereStr = where.build()

        return Select(
            tableName, columns, whereStr
        ).toString()
    }
}