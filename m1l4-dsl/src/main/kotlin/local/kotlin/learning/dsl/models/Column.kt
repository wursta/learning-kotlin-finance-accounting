package local.kotlin.learning.dsl.models

data class Column(
    val name: String,
    val alias: String? = null
) {
    override fun toString(): String {
        var str = name;
        if (alias !== null) {
            str += " as name"
        }
        return str
    }
}