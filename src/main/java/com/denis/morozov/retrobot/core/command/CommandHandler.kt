package com.denis.morozov.retrobot.core.command

import com.denis.morozov.retrobot.core.flow.FlowAssembly

class CommandHandler(
        val command: Command,
        val description: String,
        val flowAssembly: FlowAssembly
)
