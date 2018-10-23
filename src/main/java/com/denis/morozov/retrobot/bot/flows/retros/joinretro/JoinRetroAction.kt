package com.denis.morozov.retrobot.bot.flows.retros.joinretro

import com.denis.morozov.retrobot.bot.data.RetrosStorage
import com.denis.morozov.retrobot.core.database.Database
import com.denis.morozov.retrobot.core.flow.FlowAction

class JoinRetroAction(val context: JoinRetroContext,
                      val database: Database): FlowAction {

    override fun execute(userId: Long): String {
        return database.connect().use {
            RetrosStorage(it).join(userId, context.retroId!!)
            "You was joined to retro"
        }
    }
}
