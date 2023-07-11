package local.learning.common.models

@JvmInline
value class LockGuid(private val id: String) {
    fun asString() = id

    companion object {
        val NONE = LockGuid("")
    }
}