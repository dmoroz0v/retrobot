package com.denis.morozov.retrobot.core.flow.services.canceloperation

import com.denis.morozov.retrobot.core.flow.Flow
import com.denis.morozov.retrobot.core.flow.FlowAssembly

class CancelOperationFlowAssembly: FlowAssembly {

    override fun makeFlow(): Flow {
        val action = CancelOperationAction()
        return Flow(emptyArray(), action, null)
    }
}
