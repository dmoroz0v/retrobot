package com.denis.morozov.retrobot.bot

import com.denis.morozov.retrobot.telegram.Telegram

class BotController(token: String, connectionString: String) {
    private val telegram = Telegram(token)
    private val bot = Bot(connectionString)

    fun update(json: String) {
        val update = telegram.parseUpdate(json)
        val message = update?.message

        val chatId = message?.chat?.id
        val userId = message?.from?.id
        val text = message?.text

        if (chatId != null && userId != null && text != null) {

            val sendMessage = bot.update(chatId, userId, text)

            telegram.sendMessage(sendMessage)
        }
    }
}
