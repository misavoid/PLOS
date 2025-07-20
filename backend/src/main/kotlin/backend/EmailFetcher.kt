package backend

import jakarta.mail.*
import jakarta.mail.search.FlagTerm
import java.util.Properties

data class SimpleEmail(
    val messageId: String,
    val subject: String?,
    val from: String?,
    val sentDate: String?
)

fun fetchEmails(user: String, password: String): List<SimpleEmail> {
    val props = Properties()
    props["mail.imap.host"] = "imap.mail.me.com"
    props["mail.imap.port"] = "993"


    val session = Session.getDefaultInstance(props, object : Authenticator() {
        override fun getPasswordAuthentication(): PasswordAuthentication {
            return PasswordAuthentication(user, password)
        }
    })

    val store = session.getStore("imap")
    store.connect()

    val inbox = store.getFolder("INBOX")
    inbox.open(Folder.READ_ONLY)

    // only fetch unseen mails
    val messages = inbox.search(FlagTerm(Flags(Flags.Flag.SEEN), false))
    val last20 = messages.takeLast(20)

    val result = last20.map { msg -> SimpleEmail(
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
