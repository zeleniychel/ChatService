import Exceptions.ChatIdNotFoundException
import Exceptions.MessageIdNotFoundException
import Exceptions.UserIdNotFoundException

fun main() {
    ChatService.sendMessage("111", 1, 2)
    ChatService.sendMessage("5555", 1, 2)
    ChatService.sendMessage("555555555", 1, 2)
    println(ChatService.getListOfMessages(1,1,2,2))
}

object ChatService {
    private var chatsId = 0
    private var messagesId = 0
    private val chatsList = mutableListOf<Chat>()


    fun sendMessage(text: String, fromId: Int, toId: Int): Boolean {
        if (fromId == toId) {
            println("It is not possible to create a chat with yourself")
            return false
        }
        val existingChat = chatsList.find { it.users.containsAll(listOf(fromId, toId)) }
        if (existingChat != null) {

            if (existingChat.messages.last().fromId != fromId) {
                existingChat.messages.forEach { message -> if (message.fromId == toId) message.readTag = true }
            }
            existingChat.messages.add(Message(++messagesId, fromId, toId, text, false))
            println("User ID ($fromId) sent a message \"$text\" to user ID ($toId)")
            return true
        } else {
            val newChat = Chat(
                    ++chatsId,
                    mutableListOf(Message(++messagesId, fromId, toId, text, false)),
                    mutableListOf(fromId, toId)
            )
            chatsList.add(newChat)
            println("User ID ($fromId) create new chat with user ID($toId), message \"$text\" отправлено")
            return true
        }
    }

    fun editMessage(messageId: Int, text: String): Boolean {
        val existingMessage = chatsList.find { chat ->
            chat.messages.any {
                it.id == messageId } } ?: throw MessageIdNotFoundException()
        existingMessage.messages.find { it.id == messageId }?.text = text
        println("Message with ID $messageId has been changed")
        return true
    }

    fun deleteMessage(messageId: Int, userId: Int): Boolean {
        chatsList.forEach { chat ->
            chat.messages.filter { messageId == it.id && userId == it.fromId }.forEach {
                chat.messages.remove(it)
                println("Message with ID $messageId deleted")
                return true
            }
            chat.messages.filter { userId != it.fromId }.forEach {
                println("You can't delete other people's messages")
                return false
            }
        }
        throw MessageIdNotFoundException()
    }

    fun deleteChat(chatId: Int): String {
        chatsList.remove(chatsList.find { it.id == chatId } ?: throw ChatIdNotFoundException())
        return "Chat with ID $chatId deleted"
    }


    fun getUnreadChatsCount(userId: Int): String {
        var count = 0
        getChats(userId).forEach { chat -> if (chat.messages.any { !it.readTag && userId == it.toId }) count++ }
        return "User ID ($userId) have $count unread chats"
    }

    fun getChats(userId: Int): List<Chat> {
        return chatsList.filter { it.users.contains(userId) }
                .ifEmpty { throw UserIdNotFoundException() }
    }


    fun getListOfMessages(chatId: Int, messageId: Int, messageCount: Int, userId: Int): List<Message> {
        val foundedChat = chatsList
                .find { it.id == chatId }?.messages
                ?.filter { it.id >= messageId }
                ?.ifEmpty { throw MessageIdNotFoundException() }
                ?: throw ChatIdNotFoundException()


        val list = foundedChat.asSequence()
                .take(messageCount)
                .toList()

        list.filter { it.toId == userId }
                .forEach { it.readTag = true }



        chatsList.forEach { chat ->
            chat.messages.filter { message ->
                message.toId == userId
            }.forEach {
                if (it.id in messageId..list.last().id) it.readTag = true
            }
        }

        return list
    }


    fun getLastMessages(userId: Int): String {
        return getChats(userId).joinToString(", ") {
            it.messages.lastOrNull()?.text ?: "There are no messages"
        }
    }

    fun clear() {
        chatsId = 0
        messagesId = 0
        chatsList.clear()
    }

}