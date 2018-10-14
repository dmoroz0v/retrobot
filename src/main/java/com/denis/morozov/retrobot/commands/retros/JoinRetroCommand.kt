package com.denis.morozov.retrobot.commands.retros

import com.denis.morozov.retrobot.commands.Command
import com.denis.morozov.retrobot.commands.CommandResult
import com.denis.morozov.retrobot.data.*
import com.denis.morozov.retrobot.database.DatabaseConnection

class JoinRetroCommand(private val connectionString: String): Command() {

    override val key: String = "/joinretro"
    override val description: String = "Join to retro. Params: <identifier>"

    override fun execute(userId: Long, params: Array<String>): CommandResult {
        if (params.isEmpty()) {
            return CommandResult.Error("reference 'identifier' param")
        }

        val identifier = params[0].toRetroID()

        return DatabaseConnection(connectionString).use {
            RetrosStorage(it).join(userId, identifier)
            CommandResult.Success("Joined")
        }
    }
}
