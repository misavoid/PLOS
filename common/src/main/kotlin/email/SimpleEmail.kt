package email

@kotlinx.serialization.Serializable
data class SimpleEmail(
    val messageId: String,
    val subject: String?,
    val from: String?,
    val sentDate: String?
)
