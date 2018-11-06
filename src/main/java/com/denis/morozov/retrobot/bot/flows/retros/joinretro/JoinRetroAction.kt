package com.denis.morozov.retrobot.bot.flows.retros.joinretro

import com.denis.morozov.retrobot.bot.data.RetrosStorage
import com.denis.morozov.retrobot.core.database.Database
import com.denis.morozov.retrobot.core.flow.FlowAction

class JoinRetroAction(val context: JoinRetroContext,
                      val database: Database): FlowAction {

    override fun execute(userId: Long): List<String> {
        return database.connect().use {
            val retroId = context.retroId
            if (retroId != null) {
                RetrosStorage(it).join(userId, retroId)
                listOf("You was joined to retro")
            } else {
                listOf("Unexpected error")
            }
        }
    }
}
