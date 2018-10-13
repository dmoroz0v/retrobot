package com.denis.morozov.retrobot.data

import com.denis.morozov.retrobot.database.DatabaseConnection
import com.denis.morozov.retrobot.database.condition.Equal
import com.denis.morozov.retrobot.database.condition.Or
import com.denis.morozov.retrobot.database.condition.Relationship
import com.denis.morozov.retrobot.database.delete.DeleteDescriptor
import com.denis.morozov.retrobot.database.insert.InsertDescriptor
import com.denis.morozov.retrobot.database.join.JoinDescriptor
import com.denis.morozov.retrobot.database.select.SelectDescriptor
import com.denis.morozov.retrobot.database.update.UpdateDescriptor
import java.util.*

class RetrosStorage(private val connection: DatabaseConnection)
{
    fun retros(userId: String): List<Retro>
    {
        val selectDescriptor = SelectDescriptor()
        selectDescriptor.table = "Retros"
        val joinDescriptor = JoinDescriptor()
        joinDescriptor.type = JoinDescriptor.Type.LEFT
        joinDescriptor.table = "Retros_Users"
        joinDescriptor.on = Relationship("Retros.identifier", "Retros_Users.retro_identifier")
        selectDescriptor.joins = listOf(joinDescriptor)
        selectDescriptor.where = Or(
                Equal("Retros.user_id", userId),
                Equal("Retros_Users.user_id", userId)
        )

        val result  = connection.select(selectDescriptor)

        val retros: MutableList<Retro> = mutableListOf()

        result.forEach {
            retros.add(
                    Retro(
                            it["identifier"] as String,
                            it["name"] as String,
                            (it["deleted"] as Int == 1),
                            messages = emptyList()
                    )
            )
        }

        return retros
    }

    fun create(name: String, userId: String): Retro
    {
        val identifier = UUID.randomUUID().toString()
        val insertDescriptor = InsertDescriptor()
        insertDescriptor.table = "Retros"
        insertDescriptor.columns = listOf("identifier", "name", "user_id", "deleted")
        insertDescriptor.values = listOf(identifier, name, userId, 0)

        connection.insert(insertDescriptor)

        return Retro(identifier, name, false, emptyList())
    }

    fun retro(identifier: String): Retro?
    {
        val selectDescriptor = SelectDescriptor()
        selectDescriptor.table = "Retros"
        selectDescriptor.where = Equal("identifier", identifier)

        val result  = connection.select(selectDescriptor)

        return result.firstOrNull()?.let {
            Retro(
                    it["identifier"] as String,
                    it["name"] as String,
                    (it["deleted"] as Int == 1),
                    messages = emptyList()
            )
        }
    }

    fun delete(identifier: String)
    {
        val updateDescriptor = UpdateDescriptor()
        updateDescriptor.table = "Retros"
        updateDescriptor.where = Equal("identifier", identifier)
        updateDescriptor.values = mapOf("deleted" to 1)
        connection.update(updateDescriptor)
    }

    fun join(userId: String, identifier: String)
    {
        val insertDescriptor = InsertDescriptor()
        insertDescriptor.table = "Retros_Users"
        insertDescriptor.columns = listOf("retro_identifier", "user_id")
        insertDescriptor.values = listOf(identifier, userId)

        connection.insert(insertDescriptor)
    }
}
