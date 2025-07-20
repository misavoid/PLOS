package email

@kotlinx.serialization.Serializable
data class SimpleEmail(
    val messageId: String,
    val subject: String?,
    val from: String?,
    val sentDate: String?
)

// TODO: looks like the from field is currently using the adress the email is adressed to (so mine)