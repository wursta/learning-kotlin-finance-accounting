package local.learning.app.ktor

import local.learning.common.models.Principal
import local.learning.common.models.PrincipalId
import local.learning.common.models.access.Role
import local.learning.common.repo.IPrincipalRepository

class PrincipalInMemoryRepository(
    val digestFunction: (String) -> ByteArray
): IPrincipalRepository {
    private val users = listOf(
        Principal(
            id = PrincipalId("1"),
            login = "msazonov",
            password = digestFunction("qwerty"),
            role = Role.USER
        ),
        Principal(
            id = PrincipalId("2"),
            login = "vbabkin",
            password = digestFunction("qwerty"),
            role = Role.USER
        )
    )
    override fun authenticate(login: String, passwordHash: String): Principal? {
        val user = users.firstOrNull { it.login == login } ?: return null
        if (user.password.contentEquals(digestFunction(passwordHash))) {
            return user
        }
        return null
    }
    override fun getById(id: PrincipalId): Principal? = users.firstOrNull { it.id == id }
}