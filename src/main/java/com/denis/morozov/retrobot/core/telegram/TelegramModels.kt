package com.denis.morozov.retrobot.core.telegram

import com.squareup.moshi.Json

data class Update(
        val message: Message?)

data class User(
        val id: Long)

data class Message(
        val chat: Chat,
        val text: String?,
        val from: User?)

data class Chat(
        val id: Long)

data class ReplyKeyboardMarkup(
        val keyboard: List<List<KeyboardButton>>,
        @Json(name = "resize_keyboard") val resizeKeyboard: Boolean,
        @Json(name = "one_time_keyboard") val oneTimeKeyboard: Boolean)

data class KeyboardButton(
        val text: String)

data class ReplyKeyboardHide(
        @Json(name = "hide_keyboard") val hide: Boolean
)

data class SendMessage(
        @Json(name = "chat_id") val chatId: Long,
        val text: String,
        @Json(name = "reply_markup") val replyMarkup: Any? = null)
