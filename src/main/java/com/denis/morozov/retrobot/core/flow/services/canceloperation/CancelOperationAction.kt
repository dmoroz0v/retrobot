package com.denis.morozov.retrobot.core.flow.services.canceloperation

import com.denis.morozov.retrobot.core.flow.FlowAction

class CancelOperationAction: FlowAction {

    override fun execute(userId: Long): String {
        return "Current operation was cancelled"
    }
}
