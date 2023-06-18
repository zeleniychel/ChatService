import Exceptions.ChatIdNotFoundException
import Exceptions.MessageIdNotFoundException
import Exceptions.UserIdNotFoundException

fun main() {


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
                existingChat.messages.forEach { chat -> if (existingChat.messages.any{it.fromId != fromId}) chat.readTag = true }
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
        val existingMessage = chatsList.find { chat -> chat.messages.any { it.id == messageId } }?:throw MessageIdNotFoundException()
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

    fun deleteChat(chatId: Int): Boolean {
        val chat = chatsList.find { it.id == chatId }
        if (chat != null) {
            chatsList.remove(chat)
            println("Chat with ID $chatId deleted")
            return true
        }
        throw ChatIdNotFoundException()
    }


    fun getUnreadChatsCount(userId: Int): String {
        var count = 0

        val unreadChats = getChats(userId)
        unreadChats.forEach { chat -> if (chat.messages.any{!it.readTag && userId == it.toId}) count++}
        return "User ID ($userId) have $count unread chats"
    }

    fun getChats(userId: Int): MutableList<Chat> {
        val chats = chatsList.filter { it.users.contains(userId) }.toMutableList()
        if (chats.isEmpty()) throw UserIdNotFoundException()
        return chats
    }



    fun getListOfMessages(chatId: Int, messageId: Int, messageCount: Int, userId: Int): List<Message> {
        val foundedChat = chatsList.find { it.id == chatId } ?: throw ChatIdNotFoundException()

        val messageList = foundedChat.messages.filter { it.id >= messageId }
        if (messageList.isEmpty()) throw MessageIdNotFoundException()

        val list = messageList.take(messageCount)
        chatsList.forEach { chat-> chat.messages.filter { message -> message.toId == userId }.forEach { if(it.id == messageId) it.readTag = true }}
        list.filter { it.toId == userId }.forEach { it.readTag = true }
        return list.toList()
    }


    fun getLastMessages(userId: Int): MutableList<String> {
        val lastMessagesList = mutableListOf<String>()
        val chats = getChats(userId)
        for (chat in chats) {
            if (chat.messages.isEmpty()) {
                lastMessagesList.add("There are no messages")
            } else {
                lastMessagesList.add(chat.messages.last().text)
            }
        }
        return lastMessagesList
    }

    fun clear (){
        chatsId = 0
        messagesId = 0
        chatsList.clear()
    }

}
