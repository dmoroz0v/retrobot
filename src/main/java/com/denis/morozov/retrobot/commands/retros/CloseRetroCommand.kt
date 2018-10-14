package com.denis.morozov.retrobot.commands.retros

import com.denis.morozov.retrobot.commands.Command
import com.denis.morozov.retrobot.commands.CommandResult
import com.denis.morozov.retrobot.data.*
import com.denis.morozov.retrobot.database.DatabaseConnection

class CloseRetroCommand(private val connectionString: String): Command() {

    override val key: String = "/closeretro"
    override val description: String = "Close retro. Params: <identifier>"

    override fun execute(userId: Long, params: Array<String>): CommandResult {
        if (params.isEmpty()) {
            return CommandResult.Error("reference 'identifier' param")
        }

        val identifier = params[0].toRetroID()

        return DatabaseConnection(connectionString).use {

            val retrosStorage = RetrosStorage(it)

            if (retrosStorage.retro(identifier)?.userId != userId) {
                return CommandResult.Error("Retro not found")
            }

            retrosStorage.delete(identifier)
            CommandResult.Success("Retro is closed")
        }
    }
}
