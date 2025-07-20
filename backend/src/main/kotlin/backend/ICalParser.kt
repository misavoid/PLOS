package backend

import biweekly.Biweekly
import calendar.EventDto

import org.w3c.dom.Document
import java.io.StringReader
import javax.xml.parsers.DocumentBuilderFactory
import org.xml.sax.InputSource

fun extractCalendarDataFromXml(xml: String): List<String> {
    val doc: Document = DocumentBuilderFactory.newInstance()
        .newDocumentBuilder()
        .parse(InputSource(StringReader(xml)))
    val nodeList = doc.getElementsByTagName("calendar-data")
    return (0 until nodeList.length).map { nodeList.item(it).textContent }
}

fun parseIcsFromXmlResponse(xml: String): List<EventDto> {
    val calendarDataBlocks = extractCalendarDataFromXml(xml)
    return calendarDataBlocks.flatMap { ics ->
        val calendars = Biweekly.parse(ics).all()
        val calendar = calendars.firstOrNull()
        calendar?.events?.map { event ->
            EventDto(
                summary = event.summary.value ?: "No title",
                start = event.dateStart.value.toString(),
                end = event.dateEnd?.value?.toString()
            )
        } ?: emptyList()
    }
}


fun parseIcsToEvents(ics: String): List<EventDto> {
    if (!ics.trimStart().startsWith("BEGIN:VCALENDAR")) {
        println("Not an ICS file: starts with '${ics.trimStart().take(20)}'")
        return emptyList()
    }
    val calendars = Biweekly.parse(ics).all()
    val calendar = calendars.firstOrNull()
    if (calendar == null) {
        println("No valid iCalendar found in ICS data!")
        return emptyList()
    }
    return calendar.events.map { event ->
        EventDto(
            summary = event.summary.value ?: "No title",
            start = event.dateStart.value.toString(),
            end = event.dateEnd?.value?.toString()
        )
    }
}
