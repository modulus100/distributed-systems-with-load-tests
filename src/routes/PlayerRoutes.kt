package routes

import kafka.sendMessage
import models.PlayerScore
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerRecord

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
