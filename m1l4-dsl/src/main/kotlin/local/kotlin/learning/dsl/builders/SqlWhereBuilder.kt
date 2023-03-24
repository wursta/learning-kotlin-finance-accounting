package local.kotlin.learning.dsl.builders

import local.kotlin.learning.dsl.models.Condition
import local.kotlin.learning.dsl.models.ConditionsLogic
import local.kotlin.learning.dsl.models.Operator
import local.kotlin.learning.dsl.models.Where

@DslMarker
annotation class QueryWhereDsl
@QueryWhereDsl
class SqlWhereBuilder(val logic: ConditionsLogic = ConditionsLogic.AND) {
    private var conditions: MutableList<Condition> = mutableListOf()

    fun hasConditions(): Boolean = conditions.isNotEmpty()

    infix fun String.eq(value: String?) {
        var v = value
        if (v !== null) {
            v = "'$value'"
        }
        conditions.add(Condition(this, Operator.EQ, v))
    }
    infix fun String.eq(value: Number) {
        conditions.add(Condition(this, Operator.EQ, value.toString()))
    }

    infix fun String.nonEq(value: String?) {
        var v = value
        if (v !== null) {
            v = "'$value'"
        }

        conditions.add(Condition(this, Operator.NON_EQ, v))
    }
    infix fun String.nonEq(value: Number) {
        conditions.add(Condition(this, Operator.NON_EQ, value.toString()))
    }
    fun build(): Where {
        return Where(
            logic,
            conditions
        )
    }
}