package com.denis.morozov.retrobot.bot.flows.retros.myretros

import com.denis.morozov.retrobot.core.database.Database
import com.denis.morozov.retrobot.core.flow.Flow
import com.denis.morozov.retrobot.core.flow.FlowAssembly

class MyRetrosFlowAssembly(val database: Database): FlowAssembly {

    override fun makeFlow(): Flow {
        val action = MyRetrosAction(database)
        return Flow(emptyArray(), action, null)
    }
}
