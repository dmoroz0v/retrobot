package com.denis.morozov.retrobot.bot.flows.messages.messages

import com.denis.morozov.retrobot.core.database.Database
import com.denis.morozov.retrobot.core.flow.Flow
import com.denis.morozov.retrobot.core.flow.FlowAssembly
import com.denis.morozov.retrobot.core.flow.FlowInputHandler

class MessagesFlowAssembly(private val database: Database,
                           private val allMessages: Boolean): FlowAssembly {

    override fun makeFlow(): Flow {
        val context = MessagesContext()

        val inputHandlers: Array<FlowInputHandler> = arrayOf(
                RetroIdInputHandler(database, context)
        )

        val action = MessagesAction(database, context, allMessages)

        return Flow(inputHandlers, action, context)
    }
}
