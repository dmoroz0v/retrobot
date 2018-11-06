package com.denis.morozov.retrobot.bot.flows.retros.shareretro

import com.denis.morozov.retrobot.bot.data.RetrosStorage
import com.denis.morozov.retrobot.bot.flows.ChoiceRetroContext
import com.denis.morozov.retrobot.core.database.Database
import com.denis.morozov.retrobot.core.flow.FlowAction

class ShareRetroAction(val context: ChoiceRetroContext,
                       val database: Database): FlowAction {

    override fun execute(userId: Long): List<String> {
        return database.connect().use {
            val retro = RetrosStorage(it).retro(context.retroId!!)
            if (retro != null) {
                val forwardMessage = "Forward two next messages for people"
                val instructionMessage = "1. Open chat with @retrospectare_bot \n2. Call /joinretro command \n3. Paste code from next message"
                val codeMessage = retro.identifier.rawValue
                listOf(forwardMessage, instructionMessage, codeMessage)

            } else {
                listOf("Retro not found")
            }
        }
    }
}
