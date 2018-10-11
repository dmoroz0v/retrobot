package com.denis.morozov.retrobot.data

import com.denis.morozov.retrobot.database.DatabaseConnection
import com.denis.morozov.retrobot.database.condition.*
import com.denis.morozov.retrobot.database.insert.InsertDescriptor
import com.denis.morozov.retrobot.database.join.JoinDescriptor
import com.denis.morozov.retrobot.database.select.SelectDescriptor
import java.util.*

class MessagesStorage(private val connection: DatabaseConnection)
{
    fun messages(retroIdentifier: String, messageUserId: String? = null, retroUserId: String? = null): List<Message>
    {
        val selectDescriptor = SelectDescriptor()
        selectDescriptor.table = "Messages"

        val joins: MutableList<JoinDescriptor> = mutableListOf()

        val retrosJoinDescriptor = JoinDescriptor()
        retrosJoinDescriptor.type = JoinDescriptor.Type.INNER
        retrosJoinDescriptor.table = "Retros"
        retrosJoinDescriptor.on = And(
                Relationship("Retros.identifier", "Messages.retro_identifier"),
                Equal("Retros.identifier", retroIdentifier)
        )

        joins.add(retrosJoinDescriptor)

        if (retroUserId != null) {
            val joinDescriptor = JoinDescriptor()
            joinDescriptor.type = JoinDescriptor.Type.LEFT
            joinDescriptor.table = "Retros_Users"
            joinDescriptor.on = Relationship("Retros.identifier", "Retros_Users.retro_identifier")
            joins.add(joinDescriptor)
        }

        selectDescriptor.joins = joins

        val orConditions: ArrayList<ConditionDescriptor> = ArrayList()
        if (messageUserId != null) {
            orConditions.add(Equal("Messages.userId", messageUserId))
        }
        if (retroUserId != null) {
            orConditions.add(Equal("Retros.userId", retroUserId))
        }

        if (orConditions.isNotEmpty()) {
            selectDescriptor.where = Or(*orConditions.toTypedArray())
        }

        val result  = connection.select(selectDescriptor)

        val retros: MutableList<Message> = mutableListOf()

        result.forEach {
            retros.add(Message(it["identifier"] as String, it["text"] as String))
        }

        return retros
    }

    fun create(text: String, retroIdentifier: String, userId: String): Message
    {
        val identifier = UUID.randomUUID().toString()
        val insertDescriptor = InsertDescriptor()
        insertDescriptor.table = "Messages"
        insertDescriptor.columns = listOf("identifier", "text", "user_id", "retro_identifier")
        insertDescriptor.values = listOf(identifier, text, userId, retroIdentifier)

        connection.insert(insertDescriptor)

        return Message(identifier, text)
    }
}
