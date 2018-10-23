package com.denis.morozov.retrobot.bot.flows.messages.addmessage

import com.denis.morozov.retrobot.core.flow.*

class MessageTextInputHandler(private val context: AddMessageContext): FlowInputHandler {

    override fun inputMarkup(userId: Long): FlowInputHandler.InputMarkup {
        return FlowInputHandler.InputMarkup("Type message text")
    }

    override fun handle(userId: Long, text: String) {
        context.text = text
    }
}
