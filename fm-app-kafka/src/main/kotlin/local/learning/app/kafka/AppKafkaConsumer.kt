package local.learning.app.kafka

import kotlinx.atomicfu.atomic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import local.learning.api.models.IRequestDto
import local.learning.api.models.IResponseDto
import local.learning.api.serialization.utils.apiRequestDeserialize
import local.learning.api.serialization.utils.apiResponseSerialize
import local.learning.app.kafka.exception.TopicStrategyNotFound
import mu.KotlinLogging
import org.apache.kafka.clients.consumer.Consumer
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.clients.consumer.ConsumerRecords
import org.apache.kafka.clients.producer.Producer
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.errors.WakeupException
import java.time.Duration
import java.util.*

private val log = KotlinLogging.logger {}
class AppKafkaConsumer(
    private val consumer: Consumer<String, String>,
    private val producer: Producer<String, String>,
    private val topics: Map<String, TopicStrategy>
) {
    private val process = atomic(true)
    fun run() = runBlocking {
        try {
            consumer.subscribe(topics.keys)

            while(process.value) {
                val records: ConsumerRecords<String, String> = withContext(Dispatchers.IO) {
                    consumer.poll(Duration.ofSeconds(1))
                }

                if (!records.isEmpty)
                    log.info { "Receive ${records.count()} messages" }

                records.forEach { record: ConsumerRecord<String, String> ->
                    try {
                        log.info { "process ${record.key()} from ${record.topic()}:\n${record.value()}" }
                        val topicStrategy = topics[record.topic()] ?: throw TopicStrategyNotFound(record.topic())

                        val request: IRequestDto = apiRequestDeserialize(record.value())
                        val response = topicStrategy.run(request)

                        if (topicStrategy.topicsOut.isNotEmpty()) {
                            sendResponse(response, topicStrategy.topicsOut)
                        }
                    } catch (ex: Exception) {
                        // skip error
                        log.error(ex) { "error" }
                    }
                }

            }
        } catch (ex: WakeupException) {
            log.error(ex) { "error" }
            // ignore for shutdown
        } catch (ex: RuntimeException) {
            log.error(ex) { "error" }
            // exception handling
            withContext(NonCancellable) {
                throw ex
            }
        } finally {
            log.info { "exit kafka" }
            withContext(NonCancellable) {
                consumer.close()
            }
        }
    }

    fun stop() {
        process.value = false
    }
    private fun sendResponse(response: IResponseDto, topics: List<String>) {
        val json = apiResponseSerialize(response)

        topics.forEach {
            val resRecord = ProducerRecord(
                it,
                UUID.randomUUID().toString(),
                json
            )
            log.info { "sending ${resRecord.key()} to $it:\n$json" }
            producer.send(resRecord)
        }
    }
}