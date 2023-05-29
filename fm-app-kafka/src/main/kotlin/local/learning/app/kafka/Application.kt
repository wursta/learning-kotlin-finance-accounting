package local.learning.app.kafka

import mu.KotlinLogging
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.kafka.common.serialization.StringSerializer
import java.util.*

private val logger = KotlinLogging.logger {}
fun main() {
    logger.info { "Application started" }

    val config = AppKafkaConfig()
    val consumerProps = Properties().apply {
        put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, config.kafkaHosts)
        put(ConsumerConfig.GROUP_ID_CONFIG, config.kafkaGroupId)
        put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer::class.java)
        put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer::class.java)
    }

    val producerProps = Properties().apply {
        put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, config.kafkaHosts)
        put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer::class.java)
        put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer::class.java)
    }


    val consume = AppKafkaConsumer(
        KafkaConsumer(consumerProps),
        KafkaProducer(producerProps),
        AppKafkaStrategy.getDefault(config)
    )
    consume.run()
}