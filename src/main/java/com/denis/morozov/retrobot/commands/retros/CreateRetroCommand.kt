package com.denis.morozov.retrobot.commands.retros

import com.denis.morozov.retrobot.commands.Command
import com.denis.morozov.retrobot.commands.CommandResult
import com.denis.morozov.retrobot.data.RetrosStorage
import com.denis.morozov.retrobot.database.DatabaseConnection

class CreateRetroCommand(private val connectionString: String): Command() {

    override val key: String = "/createretro"
    override val description: String = "Create new retro. Params: <name>"

    override fun execute(userId: Long, params: Array<String>): CommandResult {
        if (params.isEmpty()) {
            return CommandResult.Error("reference 'name' param")
        }

        val name = params.joinToString(" ")
        return DatabaseConnection(connectionString).use {
            RetrosStorage(it).create(name, userId)
            CommandResult.Success("Retro created")
        }
    }
}
