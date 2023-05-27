package local.learning.app.kafka

import kotlinx.datetime.Clock
import local.learning.app.biz.CardProcessor
import local.learning.app.biz.ExpenseProcessor
import local.learning.common.CardContext
import local.learning.common.ExpenseContext
import local.learning.mappers.fromTransport
import local.learning.mappers.toTransport

object AppKafkaStrategy {
    fun getDefault(config: AppKafkaConfig) = mapOf(
        config.kafkaCardTopicIn to getCardStrategy(config),
        config.kafkaExpenseTopicIn to getExpenseStrategy(config)
    )
    fun getCardStrategy(config: AppKafkaConfig, processor: CardProcessor = CardProcessor()): TopicStrategy = TopicStrategy(
        processor = processor,
        topicsOut = listOf(config.kafkaCardTopicOut)
    ) {
        val context = CardContext(timeStart = Clock.System.now())
        context.fromTransport(it)
        this.processor.exec(context)
        context.toTransport()
    }

    fun getExpenseStrategy(config: AppKafkaConfig, processor: ExpenseProcessor = ExpenseProcessor()): TopicStrategy = TopicStrategy(
        processor = processor,
        topicsOut = listOf(config.kafkaExpenseTopicOut)
    ) {
        val context = ExpenseContext(timeStart = Clock.System.now())
        context.fromTransport(it)
        this.processor.exec(context)
        context.toTransport()
    }
}