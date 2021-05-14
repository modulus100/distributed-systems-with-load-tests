package ee.taltech.ee.taltech.kafka

import kotlinx.coroutines.suspendCancellableCoroutine
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.clients.producer.RecordMetadata
import org.apache.kafka.common.serialization.StringSerializer
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException


fun isRunningInsideDocker(): Boolean {
    try {
        Files.lines(Paths.get("/proc/1/cgroup")).use { stream ->
            return stream.anyMatch { line -> line.contains("/docker") }
        }
    } catch (e: IOException) {
        return false
    }
}

fun getKafkaHost(): String =
    if (isRunningInsideDocker()) "kafka:9093" else "localhost:9092"

fun <K, V> buildProducer(): KafkaProducer<K, V> =
    KafkaProducer(Properties().apply {
        this["client.id"] = "kafka-producer"
        this["bootstrap.servers"] = getKafkaHost()
        this["key.serializer"] = StringSerializer::class.java
        this["value.serializer"] = StringSerializer::class.java
    })

suspend inline fun <reified K : Any, reified V : Any> KafkaProducer<K, V>.sendMessage(record: ProducerRecord<K, V>) =
    suspendCancellableCoroutine<RecordMetadata> { continuation ->
        this.send(record) { metadata, exception ->
            if (metadata == null) continuation.resumeWithException(exception!!) else continuation.resume(metadata)
        }
    }