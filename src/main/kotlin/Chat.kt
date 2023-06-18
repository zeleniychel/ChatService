data class Chat(
    val id: Int,
    val messages: MutableList<Message>,
    val users: MutableList<Int>,
)
