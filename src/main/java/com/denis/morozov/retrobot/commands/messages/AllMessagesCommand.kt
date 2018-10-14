package com.denis.morozov.retrobot.commands.messages

import com.denis.morozov.retrobot.commands.Command
import com.denis.morozov.retrobot.commands.CommandResult
import com.denis.morozov.retrobot.data.*
import com.denis.morozov.retrobot.database.DatabaseConnection

class AllMessagesCommand(private val connectionString: String): Command() {

    override val key: String = "/allmessages"
    override val description: String = "You created retro all messages. Params: <retroIdentifier>"

    override fun execute(userId: Long, params: Array<String>): CommandResult {
        if (params.isEmpty()) {
            return CommandResult.Error("reference 'identifier' param")
        }

        val identifier = params[0].toRetroID()

        return DatabaseConnection(connectionString).use {
            val result = DatabaseConnection(connectionString).use {
                val messages = MessagesStorage(it).messages(identifier, null, userId)

                if (messages.isEmpty()) {
                    "Empty"
                } else {
                    messages.map {
                        "${it.identifier.rawValue} | ${it.text}"
                    }.joinToString("\n")
                }
            }
            CommandResult.Success(result)
        }
    }
}
