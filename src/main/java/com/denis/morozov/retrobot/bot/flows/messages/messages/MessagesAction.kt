package com.denis.morozov.retrobot.bot.flows.messages.messages

import com.denis.morozov.retrobot.bot.data.MessagesStorage
import com.denis.morozov.retrobot.bot.data.RetrosStorage
import com.denis.morozov.retrobot.bot.flows.ChoiceRetroContext
import com.denis.morozov.retrobot.core.database.Database
import com.denis.morozov.retrobot.core.flow.FlowAction

class MessagesAction(private val database: Database,
                     private val context: ChoiceRetroContext,
                     private val allMessages: Boolean): FlowAction {

    override fun execute(userId: Long): List<String> {
        return database.connect().use {
            val retroId = context.retroId
            if (retroId == null) {
                return listOf("Unexpected error")
            }
            val retroUserId: Long? = if (allMessages) userId else null
            val messageUserId: Long? = if (!allMessages) userId else null
            val messages = MessagesStorage(it).messages(retroId, messageUserId, retroUserId)
            val retro = RetrosStorage(it).retro(retroId)

            val text = if (retro != null) {
                if (messages.isEmpty()) {
                    "'${retro.name}' not have messages"
                } else {
                    val header = "Messages of '${retro.name}':\n\n"
                    header + messages.map { it.text }.joinToString("\n---------\n")
                }
            } else {
                "Unexpected error"
            }
            listOf(text)
        }
    }
}
