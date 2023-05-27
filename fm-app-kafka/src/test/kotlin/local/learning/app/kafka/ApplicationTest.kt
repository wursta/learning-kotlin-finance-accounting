package local.learning.app.kafka

import local.learning.api.models.*
import local.learning.api.serialization.utils.apiRequestSerialize
import local.learning.api.serialization.utils.apiResponseDeserialize
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.clients.consumer.MockConsumer
import org.apache.kafka.clients.consumer.OffsetResetStrategy
import org.apache.kafka.clients.producer.MockProducer
import org.apache.kafka.common.TopicPartition
import org.apache.kafka.common.serialization.StringSerializer
import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals

class ApplicationTest {
    companion object {
        const val PARTITION = 0
    }
    @Test
    fun `Card-In-Out-Topic`() {
        val consumer = MockConsumer<String, String>(OffsetResetStrategy.EARLIEST)
        val producer = MockProducer<String, String>(true, StringSerializer(), StringSerializer())

        val config = AppKafkaConfig()
        val inputTopic = config.kafkaCardTopicIn
        val outputTopic = config.kafkaCardTopicOut

        val app = AppKafkaConsumer(
            consumer,
            producer,
            mapOf(inputTopic to AppKafkaStrategy.getCardStrategy(config))
        )

        consumer.schedulePollTask {
            consumer.rebalance(Collections.singletonList(TopicPartition(inputTopic, 0)))
            consumer.addRecord(
                ConsumerRecord(
                    inputTopic,
                    PARTITION,
                    0L,
                    "test-1",
                    apiRequestSerialize(CardCreateRequestDto(
                        requestId = "uniqueRequestId",
                        card = CardCreateObjectDto(
                            number = "5191891428863955",
                            validFor = "2023-04",
                            owner = "SAZONOV MIKHAIL",
                            bank = "e69486d1-dae7-4ade-aa82-b4e98f65f0f2"
                        )
                    ))
                )
            )
            app.stop()
        }

        val startOffsets: MutableMap<TopicPartition, Long> = mutableMapOf()
        val tp = TopicPartition(inputTopic, PARTITION)
        startOffsets[tp] = 0L
        consumer.updateBeginningOffsets(startOffsets)

        app.run()

        val message = producer.history().first()
        val result = apiResponseDeserialize<CardCreateResponseDto>(message.value())
        assertEquals(outputTopic, message.topic())
        assertEquals("uniqueRequestId", result.requestId)
        assertEquals("3fa85f64-5717-4562-b3fc-2c963f66afa6", result.card?.guid)
    }

    @Test
    fun `Expense-In-Out-Topic`() {
        val consumer = MockConsumer<String, String>(OffsetResetStrategy.EARLIEST)
        val producer = MockProducer<String, String>(true, StringSerializer(), StringSerializer())

        val config = AppKafkaConfig()
        val inputTopic = config.kafkaExpenseTopicIn
        val outputTopic = config.kafkaExpenseTopicOut

        val app = AppKafkaConsumer(
            consumer,
            producer,
            mapOf(inputTopic to AppKafkaStrategy.getExpenseStrategy(config))
        )

        consumer.schedulePollTask {
            consumer.rebalance(Collections.singletonList(TopicPartition(inputTopic, 0)))
            consumer.addRecord(
                ConsumerRecord(
                    inputTopic,
                    PARTITION,
                    0L,
                    "test-1",
                    apiRequestSerialize(ExpenseCreateRequestDto(
                        requestId = "uniqueRequestId",
                        expense = ExpenseCreateObjectDto(
                            createDt = "2023-01-01T14:46:04Z",
                            amount = 540.4,
                            card = "1598044e-5259-11e9-8647-d663bd873d93",
                            category = "5410bdaf-834a-4ca6-9044-ee25d5a7164c"
                        )
                    ))
                )
            )
            app.stop()
        }

        val startOffsets: MutableMap<TopicPartition, Long> = mutableMapOf()
        val tp = TopicPartition(inputTopic, PARTITION)
        startOffsets[tp] = 0L
        consumer.updateBeginningOffsets(startOffsets)

        app.run()

        val message = producer.history().first()
        val result = apiResponseDeserialize<ExpenseCreateResponseDto>(message.value())
        assertEquals(outputTopic, message.topic())
        assertEquals("uniqueRequestId", result.requestId)
        assertEquals("3fa85f64-5717-4562-b3fc-2c963f66afa6", result.expense?.guid)
    }
}