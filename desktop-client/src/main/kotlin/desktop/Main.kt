package desktop

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.unit.dp
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import desktop.cards.CalendarCard
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
            Column(
                modifier = Modifier.fillMaxSize().padding(24.dp), // Add some overall padding!
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                EmailDashboardCard()
                Spacer(Modifier.height(16.dp))
                CalendarCard()
            }
        }
    }
}