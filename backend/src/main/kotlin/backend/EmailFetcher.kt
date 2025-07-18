package backend

import jakarta.mail.*
import java.util.Properties

data class SimpleEmail(
    val messageId: String,
    val subject: String?,
    val from: String?,
    val sentDate: String?
)

fun fetchEmails(username: String, password: String): List<SimpleEmail> {
    val props = Properties()
    props["mail.imap.host"] = "imap.mail.me.com"
    props["mail.imap.port"] = "993"
    props["mail.imap.ssl.enable"] = "true"

    val session = Session.getDefaultInstance(props, object : Authenticator() {
        override fun getPasswordAuthentication(): PasswordAuthentication {
            return PasswordAuthentication(username, password)
        }
    })

    val store = session.getStore("imap")
    store.connect()

    val inbox = store.getFolder("INBOX")
    inbox.open(Folder.READ_ONLY)

    // only fetch unseen mails
    val messages = inbox.getMessages(
        (inbox.messageCount - 400 + 1).coerceAtLeast(1),
        inbox.messageCount
    )

    val result = messages.map { msg -> SimpleEmail(
        messageId = (msg.getHeader("Message-ID")?.firstOrNull() ?: ""),
        subject = msg.subject,
        from = msg.from.joinToString { it.toString() },
        sentDate = msg.sentDate.toString()
    )
    }

inbox.close(false)
    store.close()

    return result
}
