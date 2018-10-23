package com.denis.morozov.retrobot.bot.flows.retros.closeretro

import com.denis.morozov.retrobot.core.database.Database
import com.denis.morozov.retrobot.core.flow.FlowInputHandler
import com.denis.morozov.retrobot.bot.flows.FlowInputMarkupHelper

class RetroIdInputHandler(val database: Database,
                          val context: CloseRetroContext): FlowInputHandler {

    override fun inputMarkup(userId: Long): FlowInputHandler.InputMarkup {
        val keyboard = FlowInputMarkupHelper.makeRetrosKeyboard(database, userId, true)
        return if (keyboard != null) {
            FlowInputHandler.InputMarkup("Choice retro for close", keyboard)
        } else {
            FlowInputHandler.InputMarkup("Retros not found", null, true)
        }
    }

    override fun handle(userId: Long, text: String) {
        context.retroId = FlowInputMarkupHelper.retroID(text)
    }
}
