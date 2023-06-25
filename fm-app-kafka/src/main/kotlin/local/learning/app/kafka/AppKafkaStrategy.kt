package local.learning.app.kafka

import kotlinx.coroutines.runBlocking
import local.learning.app.biz.CardProcessor
import local.learning.app.biz.ExpenseProcessor
import local.learning.common.log.ILogWrapper
import local.learning.common.models.card.CardCommand
import local.learning.common.models.expense.ExpenseCommand
import local.learning.mappers.fromTransport
import local.learning.mappers.toTransport

object AppKafkaStrategy {
    fun getDefault(config: AppKafkaConfig) = mapOf(
        config.kafkaCardTopicIn to getCardStrategy(config),
        config.kafkaExpenseTopicIn to getExpenseStrategy(config)
    )

    fun getCardStrategy(config: AppKafkaConfig, processor: CardProcessor = CardProcessor()): TopicStrategy =
        TopicStrategy(
            processor = processor,
            topicsOut = listOf(config.kafkaCardTopicOut)
        ) {
            runBlocking {
                processor.process(
                    ILogWrapper.DEFAULT,
                    logId = AppKafkaStrategy::class.qualifiedName ?: "unknown",
                    CardCommand.NONE,
                    { ctx -> ctx.fromTransport(it) },
                    { ctx -> ctx.toTransport() }
                )
            }
        }

    fun getExpenseStrategy(config: AppKafkaConfig, processor: ExpenseProcessor = ExpenseProcessor()): TopicStrategy =
        TopicStrategy(
            processor = processor,
            topicsOut = listOf(config.kafkaExpenseTopicOut)
        ) {
            runBlocking {
                processor.process(
                    ILogWrapper.DEFAULT,
                    logId = AppKafkaStrategy::class.qualifiedName ?: "unknown",
                    ExpenseCommand.NONE,
                    { ctx -> ctx.fromTransport(it) },
                    { ctx -> ctx.toTransport() }
                )
            }
        }
}