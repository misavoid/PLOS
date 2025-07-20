package calendar

import kotlinx.serialization.Serializable

@Serializable
data class EventDto(
    val summary: String,
    val start: String,
    val end: String?
)
