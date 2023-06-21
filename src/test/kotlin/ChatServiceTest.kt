import Exceptions.ChatIdNotFoundException
import Exceptions.MessageIdNotFoundException
import Exceptions.UserIdNotFoundException
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before

class ChatServiceTest {

    @Before
    fun clearBeforeTest() {
        ChatService.clear()
        ChatService.sendMessage("message",1,2)

    }

    @Test
    fun sendMessageTest() {
        val send = ChatService.sendMessage("test",1,3)
        val send2 = ChatService.sendMessage("test",1,2)
        val send3 = ChatService.sendMessage("test",2,1)
        val send4 = ChatService.sendMessage("test",1,1)
        assertEquals(true,send)
        assertEquals(true,send2)
        assertEquals(true,send3)
        assertEquals(false,send4)
    }

    @Test
    fun editMessageTest() {
        val message = ChatService.editMessage(1,"test")
        assertEquals(true, message)

    }

    @Test
    fun deleteMessageTest() {
        val message = ChatService.deleteMessage(1,1)
        assertEquals(true, message)
    }
    @Test
    fun deleteMessageTest2() {
        val message = ChatService.deleteMessage(1,2)
        assertEquals(false, message)
    }

    @Test
    fun deleteChatTest() {
        val chat = ChatService.deleteChat(1)
        assertEquals("Chat with ID 1 deleted", chat)
    }

    @Test
    fun getUnreadChatsCountTest() {
        val count = ChatService.getUnreadChatsCount(2)
        assertEquals("User ID (2) have 1 unread chats", count)
    }

    @Test
    fun getChatsTest() {
        val chats = ChatService.getChats(1)
        assertEquals(mutableListOf(Chat(1, mutableListOf(Message(1,1,2,"message", false)), mutableListOf(1,2))), chats)
    }

    @Test
    fun getListOfMessagesTest() {
        ChatService.sendMessage("message2",1,2)
        val list = ChatService.getListOfMessages(1,1,2,1)
        assertEquals(listOf(Message(1,1,2,"message",false),Message(2,1,2,"message2",false)),list)
    }

    @Test
    fun getLastMessagesTest() {
        val list = ChatService.getLastMessages(1)
        assertEquals("message", list)
    }

    @Test
    fun getLastMessagesTest2() {
        ChatService.deleteMessage(1,1)
        val list = ChatService.getLastMessages(1)
        assertEquals("There are no messages", list)
    }

    @Test(expected = ChatIdNotFoundException::class)
    fun chatIdNotFoundExceptionTest() {
        ChatService.deleteChat(5)
    }
    @Test(expected = ChatIdNotFoundException::class)
    fun chatIdNotFoundExceptionTest2() {
        ChatService.getListOfMessages(5,1,2,1)
    }

    @Test(expected = MessageIdNotFoundException::class)
    fun messageIdNotFoundExceptionTest() {
        ChatService.editMessage(7,"")
    }


    @Test(expected = UserIdNotFoundException::class)
    fun userIdNotFoundExceptionTest() {
        ChatService.getChats(5)
    }









}