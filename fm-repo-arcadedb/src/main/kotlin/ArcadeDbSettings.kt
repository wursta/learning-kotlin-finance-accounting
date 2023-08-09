package local.learning.repo.arcadedb
data class ArcadeDbSettings(
    val host: String,
    val port: Int,
    val dbName: String,
    val userName: String,
    val userPassword: String
)