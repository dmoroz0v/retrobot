package com.denis.morozov.retrobot.bot.flows.retros.closeretro

import com.denis.morozov.retrobot.core.database.Database
import com.denis.morozov.retrobot.core.flow.Flow
import com.denis.morozov.retrobot.core.flow.FlowAssembly
import com.denis.morozov.retrobot.core.flow.FlowInputHandler

class CloseRetroFlowAssembly(val database: Database): FlowAssembly {

    override fun makeFlow(): Flow {
        val context = CloseRetroContext()

        val inputHandlers: Array<FlowInputHandler> = arrayOf(
                RetroIdInputHandler(database, context)
        )

        val action = CloseRetroAction(context, database)

        return Flow(inputHandlers, action, context)
    }
}
