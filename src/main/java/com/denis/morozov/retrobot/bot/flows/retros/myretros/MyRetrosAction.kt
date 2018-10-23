package com.denis.morozov.retrobot.bot.flows.retros.myretros

import com.denis.morozov.retrobot.bot.data.RetrosStorage
import com.denis.morozov.retrobot.core.database.Database
import com.denis.morozov.retrobot.core.flow.FlowAction

class MyRetrosAction(val database: Database): FlowAction {

    override fun execute(userId: Long): String {
        return database.connect().use { databaseConnection ->
            val retros = RetrosStorage(databaseConnection).retros(userId)
            val myOwnRetrosHeader = "My own retros:\n\n"
            val myOwnRetros = retros.filter { it.userId == userId }
            val myJoinedRetrosHeader = "My joined retros:\n\n"
            val myJoinedRetros = retros.filter { it.userId != userId }

            var result = ""

            result += myOwnRetrosHeader

            if (myOwnRetros.isNotEmpty()) {
                result += myOwnRetros.map {
                    "name: ${it.name}\nidentifier: ${it.identifier.rawValue}"
                }.joinToString("\n---------\n")
            } else {
                result += "You not have own retros"
            }

            result += "\n\n\n"

            result += myJoinedRetrosHeader

            if (myJoinedRetros.isNotEmpty()) {
                result += myJoinedRetros.map {
                    "name: ${it.name}\nidentifier: ${it.identifier.rawValue}"
                }.joinToString("\n---------\n")
            } else {
                result += "You not have joined retros"
            }

            result
        }
    }
}
