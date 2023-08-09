package local.learning.common.repo

import local.learning.common.models.Principal
import local.learning.common.models.PrincipalId

interface IPrincipalRepository {
    fun authenticate(login: String, passwordHash: String): Principal?
    fun getById(id: PrincipalId): Principal?

    companion object {
        val NONE = object: IPrincipalRepository {
            override fun authenticate(login: String, passwordHash: String): Principal? {
                TODO("Not yet implemented")
            }

            override fun getById(id: PrincipalId): Principal? {
                TODO("Not yet implemented")
            }
        }
    }
}