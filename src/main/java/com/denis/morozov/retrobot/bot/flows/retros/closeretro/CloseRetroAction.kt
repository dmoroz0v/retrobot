package com.denis.morozov.retrobot.bot.flows.retros.closeretro

import com.denis.morozov.retrobot.bot.data.RetrosStorage
import com.denis.morozov.retrobot.bot.flows.ChoiceRetroContext
import com.denis.morozov.retrobot.core.database.Database
import com.denis.morozov.retrobot.core.flow.FlowAction

class CloseRetroAction(val context: ChoiceRetroContext,
                       val database: Database): FlowAction {

    override fun execute(userId: Long): List<String> {
        return database.connect().use {
            val retrosStorage = RetrosStorage(it)

            val retroId = context.retroId
            if (retroId == null) {
                return listOf("Unexpected error")
            }

            val text = if (retrosStorage.retro(retroId)?.userId != userId) {
                "Retro not found"
            } else {
                retrosStorage.delete(retroId)
                "Retro was closed"
            }
            listOf(text)
        }
    }
}
