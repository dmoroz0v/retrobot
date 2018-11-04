package com.denis.morozov.retrobot.bot.flows.retros.shareretro

import com.denis.morozov.retrobot.bot.flows.FlowInputMarkupHelper
import com.denis.morozov.retrobot.core.database.Database
import com.denis.morozov.retrobot.core.flow.FlowInputHandler

class RetroIdInputHandler(val database: Database,
                          val context: ShareRetroContext): FlowInputHandler {

    override fun inputMarkup(userId: Long): FlowInputHandler.InputMarkup {
        val keyboard = FlowInputMarkupHelper.makeRetrosKeyboard(database, userId, true)
        return if (keyboard != null) {
            FlowInputHandler.InputMarkup("Choice retro for share", keyboard)
        } else {
            FlowInputHandler.InputMarkup("Retros not found", null, true)
        }
    }

    override fun handle(userId: Long, text: String) {
        context.retroId = FlowInputMarkupHelper.retroID(text)
    }
}
