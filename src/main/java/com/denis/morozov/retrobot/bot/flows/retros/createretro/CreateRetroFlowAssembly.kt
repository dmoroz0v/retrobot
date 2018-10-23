package com.denis.morozov.retrobot.bot.flows.retros.createretro

import com.denis.morozov.retrobot.core.database.Database
import com.denis.morozov.retrobot.core.flow.Flow
import com.denis.morozov.retrobot.core.flow.FlowAssembly
import com.denis.morozov.retrobot.core.flow.FlowInputHandler

class CreateRetroFlowAssembly(val database: Database): FlowAssembly {

    override fun makeFlow(): Flow {
        val context = CreateRetroContext()

        val inputHandlers: Array<FlowInputHandler> = arrayOf(
                TypeRetroNameInputHandler(context)
        )

        val action = CreateRetroAction(context, database)

        return Flow(inputHandlers, action, context)
    }
}
