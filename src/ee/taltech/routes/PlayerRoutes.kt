package ee.taltech.routes

import ee.taltech.kafka.sendMessage
import ee.taltech.models.PlayerScore
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerRecord
import java.time.Instant

const val topic = "Topic1"

fun Route.playerRoutes(producer: KafkaProducer<Long, String>) {
    route("/player") {
        post {
            val score = call.receive<PlayerScore>()
            val scoreJson = Json.encodeToString(score)

            producer.sendMessage(ProducerRecord<Long, String>(topic, scoreJson))
            call.respond(HttpStatusCode.Accepted, score)
        }
    }
}
