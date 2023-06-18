data class Message(
    val id: Int,
    val fromId: Int,
    val toId: Int,
    var text: String,
    var readTag: Boolean
)
