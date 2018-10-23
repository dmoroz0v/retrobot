package com.denis.morozov.retrobot.bot.flows.messages.addmessage

import com.denis.morozov.retrobot.core.database.Database
import com.denis.morozov.retrobot.core.flow.*
import com.denis.morozov.retrobot.bot.flows.FlowInputMarkupHelper

class RetroIdInputHandler(private val database: Database,
                          private val context: AddMessageContext): FlowInputHandler {

    override fun inputMarkup(userId: Long): FlowInputHandler.InputMarkup {
        val keyboard = FlowInputMarkupHelper.makeRetrosKeyboard(database, userId)
        return if (keyboard != null) {
            FlowInputHandler.InputMarkup("Choice retro for add message", keyboard)
        } else {
            FlowInputHandler.InputMarkup("Retros not found", null, true)
        }
    }

    override fun handle(userId: Long, text: String) {
        context.retroId = FlowInputMarkupHelper.retroID(text)
    }
}
