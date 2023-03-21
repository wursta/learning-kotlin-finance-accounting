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
    private var whereBlockBuilders: MutableList<SqlWhereBuilder> = mutableListOf()

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
        val builder = SqlWhereBuilder()
        whereBlockBuilders.add(builder)
        builder.apply(block)
    }

    fun or(block: SqlWhereBuilder.() -> Unit) {
        val builder = SqlWhereBuilder(ConditionsLogic.OR)
        whereBlockBuilders.add(builder)
        builder.apply(block)
    }

    fun build(): Select {
        if (tableName.isEmpty()) {
            throw Exception("Не установлена таблица")
        }

        /**
         * TODO: Не очень красивая реализация блока where.
         * Нужно подумать, как сделать, чтобы функция where не добавляла сразу объект в conditions.
         * Тогда можно будет написать так: conditions.joinToString(separator = " and ") { it.build() }.
         */
        val whereBlocks = mutableListOf<Where>()
        for (c in whereBlockBuilders) {
            if (c.hasConditions()) {
                whereBlocks.add(c.build())
            }
        }

        return Select(
            tableName, columns, whereBlocks
        )
    }
}