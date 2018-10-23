package com.denis.morozov.retrobot.bot.flows.retros.joinretro

import com.denis.morozov.retrobot.core.database.Database
import com.denis.morozov.retrobot.core.flow.Flow
import com.denis.morozov.retrobot.core.flow.FlowAssembly
import com.denis.morozov.retrobot.core.flow.FlowInputHandler

class JoinRetroFlowAssembly(val database: Database): FlowAssembly {

    override fun makeFlow(): Flow {
        val context = JoinRetroContext()

        val inputHandlers: Array<FlowInputHandler> = arrayOf(
                RetroIdInputHandler(database, context)
        )

        val action = JoinRetroAction(context, database)

        return Flow(inputHandlers, action, context)
    }
}
