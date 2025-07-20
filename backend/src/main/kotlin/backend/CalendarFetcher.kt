package backend

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import java.util.Base64

// Define REPORT method, since it's not built-in to Ktor
val REPORT = HttpMethod("REPORT")

suspend fun fetchICalEvents(user: String, password: String, calendarUrl: String): String {
    val client = HttpClient(CIO)
    val authHeader = "Basic " + Base64.getEncoder().encodeToString("$user:$password".toByteArray())
    val reportBody = """
        <?xml version="1.0" encoding="UTF-8"?>
        <c:calendar-query xmlns:c="urn:ietf:params:xml:ns:caldav">
          <d:prop xmlns:d="DAV:" xmlns:cs="http://calendarserver.org/ns/">
            <d:getetag/>
            <c:calendar-data/>
          </d:prop>
          <c:filter>
            <c:comp-filter name="VCALENDAR"/>
          </c:filter>
        </c:calendar-query>
    """.trimIndent()

    val response: HttpResponse = client.request(calendarUrl) {
        method = REPORT // <-- THIS is the trick
        header(HttpHeaders.Authorization, authHeader)
        header(HttpHeaders.ContentType, "application/xml")
        header("User-Agent", "iOS/16.5.1 (20F75) dataaccessd/1.0")
        header("Depth", "1")
        setBody(reportBody)
    }

    return response.bodyAsText()
}
