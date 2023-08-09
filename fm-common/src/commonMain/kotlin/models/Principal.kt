package local.learning.common.models

import local.learning.common.models.access.Role

data class Principal(
    val id: PrincipalId = PrincipalId(""),
    val login: String = "",
    val password: ByteArray = byteArrayOf(),
    val role: Role = Role.NONE
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Principal

        if (id != other.id) return false
        if (login != other.login) return false
        return role == other.role
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + login.hashCode()
        result = 31 * result + role.hashCode()
        return result
    }

    companion object {
        val NONE = Principal()
    }
}
