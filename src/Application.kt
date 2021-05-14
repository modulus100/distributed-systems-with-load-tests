package ee.taltech

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.serialization.*
import io.ktor.server.engine.*
import ee.taltech.ee.taltech.kafka.buildProducer
import org.slf4j.event.Level
import ee.taltech.ee.taltech.routes.baseRoutes
import ee.taltech.ee.taltech.routes.playerRoutes

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
fun Application.module() {
    install(ContentNegotiation) {
        json()
    }

    install(AutoHeadResponse)

    install(CallLogging) {
        level = Level.INFO
        filter { call -> call.request.path().startsWith("/") }
    }

    install(CORS) {
        method(HttpMethod.Options)
        method(HttpMethod.Put)
        method(HttpMethod.Delete)
        method(HttpMethod.Patch)
//        header(HttpHeaders.Authorization)
//        header("MyCustomHeader")
//        allowCredentials = true
        anyHost() // @TODO: Don't do this in production if possible. Try to limit it.
    }

    install(ShutDownUrl.ApplicationCallFeature) {
        // The URL that will be intercepted (you can also use the application.conf's ktor.deployment.shutdown.url key)
        shutDownUrl = "/ktor/application/shutdown"
        // A function that will be executed to get the exit code of the process
        exitCodeSupplier = { 0 } // ApplicationCall.() -> Int
    }

    install(StatusPages) {
        exception<AuthenticationException> { cause ->
            call.respond(HttpStatusCode.Unauthorized)
        }
        exception<AuthorizationException> { cause ->
            call.respond(HttpStatusCode.Forbidden)
        }
    }

    // Routes
    install(Routing) {
        baseRoutes()
        playerRoutes(buildProducer())
    }
}

class AuthenticationException : RuntimeException()
class AuthorizationException : RuntimeException()