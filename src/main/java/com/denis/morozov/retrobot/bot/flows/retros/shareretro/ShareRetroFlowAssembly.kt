package com.denis.morozov.retrobot.bot.flows.retros.shareretro

import com.denis.morozov.retrobot.bot.flows.ChoiceRetroContext
import com.denis.morozov.retrobot.bot.flows.ChoiceRetroInputHandler
import com.denis.morozov.retrobot.core.database.Database
import com.denis.morozov.retrobot.core.flow.Flow
import com.denis.morozov.retrobot.core.flow.FlowAssembly
import com.denis.morozov.retrobot.core.flow.FlowInputHandler

class ShareRetroFlowAssembly(val database: Database): FlowAssembly {

    override fun makeFlow(): Flow {
        val context = ChoiceRetroContext()

        val inputHandlers: Array<FlowInputHandler> = arrayOf(
                ChoiceRetroInputHandler(database, context, false, "Choice retro for share")
        )

        val action = ShareRetroAction(context, database)

        return Flow(inputHandlers, action, context)
    }
}
