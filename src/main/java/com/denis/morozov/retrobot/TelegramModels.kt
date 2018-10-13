package com.denis.morozov.retrobot

data class Update(
        val message: Message?
)

data class User(
        val id: Long
)

data class Message(
        val chat: Chat,
        val text: String?,
        val from: User?
)

data class Chat(
        val id: Long
)
