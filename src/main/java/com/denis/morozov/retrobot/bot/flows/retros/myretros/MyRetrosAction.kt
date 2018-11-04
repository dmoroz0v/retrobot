package com.denis.morozov.retrobot.bot.flows.retros.myretros

import com.denis.morozov.retrobot.bot.data.RetrosStorage
import com.denis.morozov.retrobot.core.database.Database
import com.denis.morozov.retrobot.core.flow.FlowAction

class MyRetrosAction(val database: Database): FlowAction {

    override fun execute(userId: Long): List<String> {
        return database.connect().use { databaseConnection ->
            val retros = RetrosStorage(databaseConnection).retros(userId)

            // own retros

            val myOwnRetrosHeader = "My own retros:\n\n"
            val myOwnRetros = retros.filter { it.userId == userId }

            var myOwnRetrosString = ""

            myOwnRetrosString += myOwnRetrosHeader

            if (myOwnRetros.isNotEmpty()) {
                myOwnRetrosString += myOwnRetros.map {
                    (myOwnRetros.indexOf(it) + 1).toString() + ". " + it.name
                }.joinToString("\n")
            } else {
                myOwnRetrosString += "You not have own retros"
            }

            // joined retros

            val myJoinedRetrosHeader = "My joined retros:\n\n"
            val myJoinedRetros = retros.filter { it.userId != userId }

            var myJoinedRetrosString = ""

            myJoinedRetrosString += myJoinedRetrosHeader

            if (myJoinedRetros.isNotEmpty()) {
                myJoinedRetrosString += myJoinedRetros.map {
                    (myJoinedRetros.indexOf(it) + 1).toString() + ". " + it.name
                }.joinToString("\n")
            } else {
                myJoinedRetrosString += "You not have joined retros"
            }

            // result

            listOf(myOwnRetrosString, myJoinedRetrosString)
        }
    }
}
