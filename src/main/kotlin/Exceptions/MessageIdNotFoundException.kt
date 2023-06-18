package Exceptions

class MessageIdNotFoundException (message:String = "Message with this ID not found") : RuntimeException(message) {
}