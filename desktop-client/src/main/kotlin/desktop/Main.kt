package desktop

import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import desktop.cards.EmailDashboardCard
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.client.call.body
import kotlinx.serialization.Serializable


@Serializable
data class Message(val text: String)

fun main() = application {
    val client = remember {
        HttpClient(CIO) {
            install(ContentNegotiation) { json() }
        }
    }

    var message by remember { mutableStateOf("Loading...") }

    LaunchedEffect(Unit) {
        try {
            val response: Message = client.get("http://localhost:8080/hello").body()
            println("Response received: $response")
            message = response.text
        } catch (e: Exception) {
            println("Error: ${e.localizedMessage}")
            message = "Failed to connect. Please check your connection and try again."
        }
    }

    Window(onCloseRequest = ::exitApplication, title = "PLOS Dashboard") {
        MaterialTheme {
            EmailDashboardCard()
        }
        Text(message)
    }
}