package com.denis.morozov.retrobot.commands.retros

import com.denis.morozov.retrobot.commands.Command
import com.denis.morozov.retrobot.commands.CommandResult
import com.denis.morozov.retrobot.data.RetrosStorage
import com.denis.morozov.retrobot.database.DatabaseConnection

class MyRetrosCommand(private val connectionString: String): Command() {

    override val key: String = "/myretros"
    override val description: String = "Print all you created and joined retros"

    override fun execute(userId: Long, params: Array<String>): CommandResult {

        return DatabaseConnection(connectionString).use {
            val retros = RetrosStorage(it).retros(userId)

            val result = if (retros.isEmpty()) {
                "Empty"
            } else {
                retros.map {
                    "name: ${it.name}\nidentifier: ${it.identifier.rawValue}\nowner: ${it.userId == userId}"
                }.joinToString("\n---------\n")
            }

            CommandResult.Success(result)
        }
    }
}
