package com.denis.morozov.retrobot.bot.flows.retros.closeretro

import com.denis.morozov.retrobot.bot.data.RetrosStorage
import com.denis.morozov.retrobot.core.database.Database
import com.denis.morozov.retrobot.core.flow.FlowAction

class CloseRetroAction(val context: CloseRetroContext,
                       val database: Database): FlowAction {

    override fun execute(userId: Long): List<String> {
        return database.connect().use {
            val retrosStorage = RetrosStorage(it)

            val text = if (retrosStorage.retro(context.retroId!!)?.userId != userId) {
                "Retro not found"
            } else {
                retrosStorage.delete(context.retroId!!)
                "Retro was closed"
            }
            listOf(text)
        }
    }
}
