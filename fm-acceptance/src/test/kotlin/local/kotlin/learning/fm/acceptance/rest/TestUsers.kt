package local.kotlin.learning.fm.acceptance.rest

data class TestUser(
    val login: String,
    val password: String
)

val TEST_USER_1 = TestUser("msazonov", "qwerty")
val TEST_USER_2 = TestUser("vbabkin", "qwerty")