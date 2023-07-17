import com.arcadedb.remote.RemoteDatabase

object ArcadeDbSchema {
    fun initialize(
        host: String,
        port: Int,
        dbName: String,
        userName: String,
        userPassword: String
    ) {
        val db = RemoteDatabase(host, port, dbName, userName, userPassword)
        if (!db.exists()) {
            db.create()
        }

        val changelogScript = this::class.java.getResource("changelog.sql")?.readText()
            ?: throw Exception("File changelog.sql not found")

        db.command("sqlscript", changelogScript)
    }

    fun clear(
        host: String,
        port: Int,
        dbName: String,
        userName: String,
        userPassword: String
    )
    {
        val db = RemoteDatabase(host, port, dbName, userName, userPassword)

        val script = """
    DELETE FROM card;
    DELETE FROM expense;   
""".trimIndent()

        db.command("sqlscript", script)
    }
}