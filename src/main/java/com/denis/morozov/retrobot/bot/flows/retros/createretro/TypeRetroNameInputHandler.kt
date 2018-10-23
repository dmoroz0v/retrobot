package com.denis.morozov.retrobot.bot.flows.retros.createretro

import com.denis.morozov.retrobot.core.flow.FlowInputHandler

class TypeRetroNameInputHandler(val context: CreateRetroContext): FlowInputHandler {

    override fun inputMarkup(userId: Long): FlowInputHandler.InputMarkup {
        return FlowInputHandler.InputMarkup("Type retro name")
    }

    override fun handle(userId: Long, text: String) {
        context.name = text
    }
}
