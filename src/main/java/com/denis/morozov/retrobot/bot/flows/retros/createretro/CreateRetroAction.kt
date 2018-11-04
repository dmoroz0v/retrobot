package com.denis.morozov.retrobot.bot.flows.retros.createretro

import com.denis.morozov.retrobot.bot.data.RetrosStorage
import com.denis.morozov.retrobot.core.database.Database
import com.denis.morozov.retrobot.core.flow.FlowAction

class CreateRetroAction(val context: CreateRetroContext,
                        val database: Database): FlowAction {

    override fun execute(userId: Long): List<String> {
        return database.connect().use {
            RetrosStorage(it).create(context.name!!, userId)
            listOf("Retro was created")
        }
    }
}
