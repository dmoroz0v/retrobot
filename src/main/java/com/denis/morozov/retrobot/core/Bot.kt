package com.denis.morozov.retrobot.core

import com.denis.morozov.retrobot.core.telegram.ReplyKeyboardHide
import com.denis.morozov.retrobot.core.telegram.SendMessage
import com.denis.morozov.retrobot.core.command.Command
import com.denis.morozov.retrobot.core.command.CommandHandler
import com.denis.morozov.retrobot.core.database.Database
import com.denis.morozov.retrobot.core.flow.FlowController
import com.denis.morozov.retrobot.core.telegram.Telegram

class Bot(token: String,
          private val database: Database,
          private val commandsHandlers: List<CommandHandler>) {

    private val telegram = Telegram(token)

    fun update(json: String) {
        val update = telegram.parseUpdate(json)
        val message = update?.message

        val chatId = message?.chat?.id
        val userId = message?.from?.id
        val text = message?.text

        if (chatId != null && userId != null && text != null) {

            val sendMessages = update(chatId, userId, text)

            sendMessages.forEach {
                telegram.sendMessage(it)
            }
        }
    }

    private fun update(chatId: Long, userId: Long, text: String): List<SendMessage> {

        val flowController = FlowController(database, userId, commandsHandlers)

        if (text.startsWith("/")) {
            flowController.start(Command(text))
        } else {
            flowController.restore()
        }

        val result = flowController.handleUpdate(text)

        if (result.finished) {
            flowController.clear()
        } else {
            flowController.store()
        }

        return result.texts.map {
            SendMessage(chatId, it, result.keyboard ?: ReplyKeyboardHide(true))
        }
    }
}
