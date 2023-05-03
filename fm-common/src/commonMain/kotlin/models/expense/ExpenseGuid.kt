package local.learning.common.models.expense

@JvmInline
value class ExpenseGuid(private val guid: String) {
    fun asString() = guid
    fun isValid(): Boolean = !guid.matches(Regex("[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}"))
    companion object {
        val NONE = ExpenseGuid("")
    }
}