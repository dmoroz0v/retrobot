package com.denis.morozov.retrobot.bot.flows.messages.addmessage

import com.denis.morozov.retrobot.bot.data.MessagesStorage
import com.denis.morozov.retrobot.bot.data.RetrosStorage
import com.denis.morozov.retrobot.core.database.Database
import com.denis.morozov.retrobot.core.flow.*

class AddMessageAction(private val database: Database,
                       private val context: AddMessageContext): FlowAction {

    override fun execute(userId: Long): List<String> {
        return database.connect().use {
            val userRetros = RetrosStorage(it).retros(userId)
            val text = if (userRetros.firstOrNull { it.identifier == context.choiceRetroContext.retroId!! } != null) {
                MessagesStorage(it).create(context.text!!, context.choiceRetroContext.retroId!!, userId)
                "Message was added"
            } else {
                "Retro not found"
            }
            listOf(text)
        }
    }
}
