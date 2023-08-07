package local.learning.common.models

@JvmInline
value class PrincipalId(private val id: String) {
    fun asString() = id

    companion object {
        val NONE = PrincipalId("")
    }
}