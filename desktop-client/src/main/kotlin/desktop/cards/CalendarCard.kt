package desktop.cards

import calendar.EventDto
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.*
import kotlinx.serialization.*
import kotlinx.serialization.json.*
import java.net.URL



@Composable
fun CalendarCard() {
    var events by remember { mutableStateOf<List<EventDto>>(emptyList()) }
    var loading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }
    var refreshTrigger by remember { mutableStateOf(0) }

    fun refresh() {
        refreshTrigger++ // Changing this triggers LaunchedEffect
    }

    LaunchedEffect(refreshTrigger) {
        loading = true
        error = null
        try {
            val json = withContext(Dispatchers.IO) {
                URL("http://localhost:8080/calendar").readText()
            }
            events = Json.decodeFromString(json)
        } catch (e: Exception) {
            error = e.message
            events = emptyList()
        }
        loading = false
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(8.dp, shape = MaterialTheme.shapes.extraLarge),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFF)),
        shape = MaterialTheme.shapes.extraLarge,
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            Modifier
                .padding(24.dp)
                .fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    "🗓️ Patti's Calendar",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.weight(1f)
                )
                Button(onClick = { refresh() }) {
                    Text("Refresh")
                }
            }
            Spacer(Modifier.height(16.dp))
            when {
                loading -> CircularProgressIndicator(Modifier.align(Alignment.CenterHorizontally))
                error != null -> Text("Error: $error", color = MaterialTheme.colorScheme.error)
                events.isEmpty() -> Text("No events found.")
                else -> LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(events) { event ->
                        Column(
                            Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        ) {
                            Text(
                                event.summary,
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.secondary
                            )
                            Text(
                                "Start: ${event.start}",
                                style = MaterialTheme.typography.bodySmall
                            )
                            event.end?.let {
                                Text(
                                    "End: $it",
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                            Divider()
                        }
                    }
                }
            }
        }
    }
}