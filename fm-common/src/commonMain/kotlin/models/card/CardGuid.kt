package local.learning.common.models.card

@JvmInline
value class CardGuid(private val guid: String) {
    fun asString() = guid

    companion object {
        val NONE = CardGuid("")
    }
}