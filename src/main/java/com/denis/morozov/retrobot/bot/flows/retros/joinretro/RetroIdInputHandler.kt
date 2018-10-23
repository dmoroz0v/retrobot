package com.denis.morozov.retrobot.bot.flows.retros.joinretro

import com.denis.morozov.retrobot.bot.data.Retro
import com.denis.morozov.retrobot.core.database.Database
import com.denis.morozov.retrobot.core.flow.FlowInputHandler

class RetroIdInputHandler(val database: Database,
                          val context: JoinRetroContext): FlowInputHandler {

    override fun inputMarkup(userId: Long): FlowInputHandler.InputMarkup {
        return FlowInputHandler.InputMarkup("Type retro id", null, false)
    }

    override fun handle(userId: Long, text: String) {
        context.retroId = Retro.ID(text)
    }
}
