package com.denis.morozov.retrobot.commands.messages

import com.denis.morozov.retrobot.commands.Command
import com.denis.morozov.retrobot.commands.CommandResult
import com.denis.morozov.retrobot.data.*
import com.denis.morozov.retrobot.database.DatabaseConnection

class MyMessagesCommand(private val connectionString: String): Command() {

    override val key: String = "/mymessages"
    override val description: String = "You retro messages. Params: <retroIdentifier>"

    override fun execute(userId: Long, params: Array<String>): CommandResult {
        if (params.isEmpty()) {
            return CommandResult.Error("reference 'identifier' param")
        }

        val identifier = params[0].toRetroID()

        return DatabaseConnection(connectionString).use {
            val result = DatabaseConnection(connectionString).use {
                val messages = MessagesStorage(it).messages(identifier, userId, null)

                if (messages.isEmpty()) {
                    "Empty"
                } else {
                    messages.map { it.text }.joinToString("\n---------\n")
                }
            }
            CommandResult.Success(result)
        }
    }
}
