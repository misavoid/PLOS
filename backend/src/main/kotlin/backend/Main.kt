package backend

import calendar.EventDto

import io.ktor.http.HttpStatusCode
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.server.response.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.Serializable
import io.github.cdimascio.dotenv.dotenv
import io.ktor.http.ContentType


fun main() {
    embeddedServer(Netty, port = 8080) {
        install(ContentNegotiation) { json() }
        System.setProperty("mail.debug", "true")

        routing {
            get("/hello") {
                call.respond(Message("Hello from the PLOS backend 🚀"))
            }

            get("/test-emails") {
                val dotenv = dotenv()
                val user = dotenv["APPLE_USERNAME"]
                val password = dotenv["APPSPECIFIC_PASSWORD_PLOS"]

                if (user.isNullOrBlank() || password.isNullOrBlank()) {
                    call.respondText("Missing environment variables!", status = HttpStatusCode.InternalServerError)
                    return@get
                }

                println("User: >$user< (${user.length} chars)")
                println("Password: (${password.length} chars)")

                val emails = fetchEmails(
                    user = user,
                    password = password
                ).toList()
                call.respond(emails.toList())
            }

            get("/calendar") {
                try {
                    val dotenv = dotenv()
                    val user = dotenv["APPLE_USERNAME"] ?: error("No user set")
                    val password = dotenv["APPSPECIFIC_PASSWORD_PLOS"] ?: error("No password set")
                    val calendarUrl = dotenv["PLOS_CALENDAR_URL"] ?: error("No calendar URL set")

                    val xml = fetchICalEvents(user, password, calendarUrl)
                    println("Fetched calendar XML: ${xml.take(250)}...") // show first 250 chars for debugging


                    val events = parseIcsFromXmlResponse(xml)
                    println("Parsed events: $events")

                    call.respond(events)
                } catch (e: Exception) {
                    e.printStackTrace() // print to backend logs
                    call.respondText("Server error: ${e.message}", status = HttpStatusCode.InternalServerError)
                }
            }

        }
    }.start(wait = true)
}

@Serializable
data class Message(val text: String)

