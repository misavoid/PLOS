package backend

import jakarta.mail.*
import jakarta.mail.search.FlagTerm
import java.util.Properties

@kotlinx.serialization.Serializable
data class SimpleEmail(
    val messageId: String,
    val subject: String?,
    val from: String?,
    val sentDate: String?
)

fun fetchEmails(user: String, password: String): List<SimpleEmail> {
    // Enable JavaMail debug output
    System.setProperty("mail.debug", "true")

    val t0 = System.currentTimeMillis()
    println("=== Starting fetchEmails ===")

    val props = Properties()
    props["mail.store.protocol"] = "imaps"
    props["mail.imaps.host"] = "imap.mail.me.com"
    props["mail.imaps.port"] = "993"
    props["mail.imaps.connectiontimeout"] = "5000"
    props["mail.imaps.timeout"] = "5000"

    println("User: >$user< (length: ${user.length})")
    println("Password length: ${password.length}")

    try {
        val session = Session.getInstance(props)
        val store = session.getStore("imaps")
        println("Before store.connect() [${System.currentTimeMillis() - t0} ms]")
        store.connect("imap.mail.me.com", 993, user, password)
        println("After store.connect() [${System.currentTimeMillis() - t0} ms]")

        // List all folders at root level
        val root = store.defaultFolder
        println("Root folder: ${root.fullName} [Type: ${root.type}]")
        val folders = root.list()
        if (folders.isEmpty()) {
            println("No folders found!")
        } else {
            println("Listing folders:")
            folders.forEach { println("  Folder: '${it.fullName}' (Type: ${it.type})") }
        }

        // Try all common inbox names if 'Inbox' fails
        val possibleInboxNames = listOf("INBOX", "Inbox", "Posteingang")
        var inbox: Folder? = null
        for (name in possibleInboxNames) {
            try {
                println("Trying to open folder '$name'...")
                val candidate = store.getFolder(name)
                candidate.open(Folder.READ_ONLY)
                println("Success opening folder '$name'")
                inbox = candidate
                break
            } catch (e: Exception) {
                println("Failed to open folder '$name': ${e.message}")
            }
        }
        if (inbox == null) {
            println("ERROR: Could not open any common inbox folder.")
            store.close()
            return emptyList()
        }

        println("After opening inbox: [${System.currentTimeMillis() - t0} ms]")

        // Search for unread messages
        val unreadMessages = inbox.search(FlagTerm(Flags(Flags.Flag.SEEN), false))
        println("Unread messages found: ${unreadMessages.size} [${System.currentTimeMillis() - t0} ms]")

        // Get the last unread message, or null if none
        val msg = unreadMessages.lastOrNull()

        val result: MutableList<SimpleEmail> = if (msg != null) {
            arrayListOf(
                SimpleEmail(
                    messageId = (msg.getHeader("Message-ID")?.firstOrNull() ?: ""),
                    subject = msg.subject,
                    from = msg.from.joinToString { it.toString() },
                    sentDate = msg.sentDate.toString()
                )
            )
        } else {
            arrayListOf()
        }


        inbox.close(false)
        store.close()
        println("=== fetchEmails finished successfully [${System.currentTimeMillis() - t0} ms] ===")
        return result

    } catch (e: Exception) {
        println("Exception in fetchEmails: ${e.message}")
        e.printStackTrace()
        return emptyList()
    }
}
