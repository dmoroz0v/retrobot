package com.denis.morozov.retrobot.commands.messages

import com.denis.morozov.retrobot.commands.Command
import com.denis.morozov.retrobot.commands.CommandResult
import com.denis.morozov.retrobot.data.*
import com.denis.morozov.retrobot.database.DatabaseConnection

class AddMessageCommand(private val connectionString: String): Command() {

    override val key: String = "/addmessage"
    override val description: String = "Add message to retro. Params: <retroIdentifier> <text>"

    override fun execute(userId: Long, params: Array<String>): CommandResult {
        if (params.size < 2) {
            return CommandResult.Error("reference 'text' param and 'identifier' param")
        }

        val identifier = params[0].toRetroID()
        val text = params.drop(1).joinToString(" ")

        return DatabaseConnection(connectionString).use {
            val result = DatabaseConnection(connectionString).use {
                val userRetros = RetrosStorage(it).retros(userId)
                if (userRetros.firstOrNull { it.identifier == identifier } != null) {
                    MessagesStorage(it).create(text, identifier, userId)
                    "Message added"
                } else {
                    "Retro not found"
                }
            }
            CommandResult.Success(result)
        }
    }
}
