package desktop.cards

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import email.SimpleEmail // <-- from common module
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.*
import kotlinx.serialization.json.Json

@Composable
fun EmailDashboardCard() {
    val httpClient = remember {
        HttpClient(CIO) {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
        }
    }

    var email by remember { mutableStateOf<SimpleEmail?>(null) }
    var loading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }

    // Fetch latest unread email
    fun refresh() {
        loading = true
        error = null
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val emails: List<SimpleEmail> = httpClient.get("http://localhost:8080/test-emails").body()
                    email = emails.lastOrNull()
                    loading = false

            } catch (e: Exception) {
                    error = "Failed to fetch email: ${e.message}"
                    loading = false
                }
            }
        }


    // Fetch once on startup
    LaunchedEffect(Unit) { refresh() }

    Card(
        modifier = Modifier.padding(16.dp).fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Text("📥 Latest Unread Email", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))

            when {
                loading -> Text("Loading...", color = MaterialTheme.colorScheme.primary)
                error != null -> Text("Error: $error", color = MaterialTheme.colorScheme.error)
                email == null -> Text("No unread emails found.", color = MaterialTheme.colorScheme.secondary)
                else -> {
                    Text("From: ${email?.from}", style = MaterialTheme.typography.bodyMedium)
                    Text("Subject: ${email?.subject}", style = MaterialTheme.typography.bodyMedium)
                    Text("Date: ${email?.sentDate}", style = MaterialTheme.typography.bodySmall)
                }
            }

            Spacer(Modifier.height(8.dp))
            Button(onClick = { refresh() }, enabled = !loading) {
                Text("Refresh")
            }
        }
    }
}
