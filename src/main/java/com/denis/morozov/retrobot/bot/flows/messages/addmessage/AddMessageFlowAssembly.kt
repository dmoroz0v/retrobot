package com.denis.morozov.retrobot.bot.flows.messages.addmessage

import com.denis.morozov.retrobot.bot.flows.ChoiceRetroInputHandler
import com.denis.morozov.retrobot.core.database.Database
import com.denis.morozov.retrobot.core.flow.Flow
import com.denis.morozov.retrobot.core.flow.FlowAssembly

class AddMessageFlowAssembly(val database: Database): FlowAssembly {

    override fun makeFlow(): Flow {
        val context = AddMessageContext()

        val inputHandlers = arrayOf(
                MessageTextInputHandler(context),
                ChoiceRetroInputHandler(database, context.choiceRetroContext, false, "Choice retro for add message")
        )

        val addMessageAction = AddMessageAction(database, context)

        return Flow(inputHandlers, addMessageAction, context)
    }
}
