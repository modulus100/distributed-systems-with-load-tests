package ee.taltech.modules

import ee.taltech.routes.registerBaseRoutes
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.serialization.*
import io.ktor.server.engine.*
import org.slf4j.event.Level

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    // Dependencies
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
    registerBaseRoutes()

    // Start stop handlers
    environment.monitor.subscribe(ApplicationStarted) {
        println("My app is ready to roll")
    }

    environment.monitor.subscribe(ApplicationStopped) {
        println("Time to clean up")
    }
}

class AuthenticationException : RuntimeException()
class AuthorizationException : RuntimeException()