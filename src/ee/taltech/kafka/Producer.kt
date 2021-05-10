package ee.taltech.kafka

import kotlinx.coroutines.suspendCancellableCoroutine
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.clients.producer.RecordMetadata
import org.apache.kafka.common.serialization.StringSerializer
import java.util.*
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

fun <K, V> buildProducer(): KafkaProducer<K, V> =
    KafkaProducer(Properties().apply {
        this["bootstrap.servers"] = "localhost:9092"
        this["key.serializer"] = StringSerializer::class.java
        this["value.serializer"] = StringSerializer::class.java
    })

suspend inline fun <reified K : Any, reified V : Any> KafkaProducer<K, V>.sendMessage(record: ProducerRecord<K, V>) =
    suspendCancellableCoroutine<RecordMetadata> { continuation ->
        this.send(record) { metadata, exception ->
            if (metadata == null) continuation.resumeWithException(exception!!) else continuation.resume(metadata)
        }
    }