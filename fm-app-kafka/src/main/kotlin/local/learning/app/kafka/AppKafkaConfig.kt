package local.learning.app.kafka

class AppKafkaConfig(
    val kafkaHosts: List<String> = KAFKA_HOSTS,
    val kafkaGroupId: String = KAFKA_GROUP_ID,
    val kafkaCardTopicIn: String = KAFKA_CARD_TOPIC_IN,
    val kafkaExpenseTopicIn: String = KAFKA_EXPENSE_TOPIC_IN,
    val kafkaCardTopicOut: String = KAFKA_CARD_TOPIC_OUT,
    val kafkaExpenseTopicOut: String = KAFKA_EXPENSE_TOPIC_OUT
) {
    companion object {
        private const val KAFKA_HOST_VAR = "KAFKA_HOSTS"
        private const val KAFKA_CARD_TOPIC_IN_VAR = "KAFKA_CARD_TOPIC_IN"
        private const val KAFKA_CARD_TOPIC_OUT_VAR = "KAFKA_CARD_TOPIC_OUT"
        private const val KAFKA_EXPENSE_TOPIC_IN_VAR = "KAFKA_CARD_TOPIC_IN"
        private const val KAFKA_EXPENSE_TOPIC_OUT_VAR = "KAFKA_CARD_TOPIC_OUT"
        private const val KAFKA_GROUP_ID_VAR = "KAFKA_GROUP_ID"

        val KAFKA_HOSTS by lazy { (System.getenv(KAFKA_HOST_VAR) ?: "").split("\\s*[,;]\\s*") }
        val KAFKA_GROUP_ID by lazy { System.getenv(KAFKA_GROUP_ID_VAR) ?: "finance-management" }
        val KAFKA_CARD_TOPIC_IN by lazy { System.getenv(KAFKA_CARD_TOPIC_IN_VAR) ?: "fm-card-in" }
        val KAFKA_CARD_TOPIC_OUT by lazy { System.getenv(KAFKA_CARD_TOPIC_OUT_VAR) ?: "fm-card-out" }
        val KAFKA_EXPENSE_TOPIC_IN by lazy { System.getenv(KAFKA_EXPENSE_TOPIC_IN_VAR) ?: "fm-expense-in" }
        val KAFKA_EXPENSE_TOPIC_OUT by lazy { System.getenv(KAFKA_EXPENSE_TOPIC_OUT_VAR) ?: "fm-expense-out" }
    }
}