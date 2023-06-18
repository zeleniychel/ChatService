package Exceptions

class ChatIdNotFoundException (message:String = "Chat with this ID not found") : RuntimeException(message) {
}