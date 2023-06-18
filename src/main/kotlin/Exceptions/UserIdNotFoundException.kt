package Exceptions

class UserIdNotFoundException (message:String = "User with this ID not found") : RuntimeException(message) {
}