package com.denis.morozov.retrobot.bot.data

import com.denis.morozov.retrobot.core.database.DatabaseConnection
import com.denis.morozov.retrobot.core.database.condition.And
import com.denis.morozov.retrobot.core.database.condition.Equal
import com.denis.morozov.retrobot.core.database.condition.Or
import com.denis.morozov.retrobot.core.database.condition.Relationship
import com.denis.morozov.retrobot.core.database.insert.InsertDescriptor
import com.denis.morozov.retrobot.core.database.join.JoinDescriptor
import com.denis.morozov.retrobot.core.database.select.SelectDescriptor
import com.denis.morozov.retrobot.core.database.update.UpdateDescriptor
import java.util.*

class RetrosStorage(private val connection: DatabaseConnection)
{
    fun retros(userId: Long): List<Retro>
    {
        val selectDescriptor = SelectDescriptor()
        selectDescriptor.columns = listOf("Retros.identifier", "Retros.name", "Retros.deleted", "Retros.user_id")
        selectDescriptor.table = "Retros"
        val joinDescriptor = JoinDescriptor()
        joinDescriptor.type = JoinDescriptor.Type.LEFT
        joinDescriptor.table = "Retros_Users"
        joinDescriptor.on = Relationship("Retros.identifier", "Retros_Users.retro_identifier")
        selectDescriptor.joins = listOf(joinDescriptor)
        selectDescriptor.where = And(
                Or(
                    Equal("Retros.user_id", userId),
                    Equal("Retros_Users.user_id", userId)
                ),
                Equal("Retros.deleted", 0)
        )

        val result  = connection.select(selectDescriptor)

        val retros: MutableList<Retro> = mutableListOf()

        result.forEach {
            retros.add(
                    Retro(
                            Retro.ID(it["identifier"] as String),
                            it["name"] as String,
                            (it["deleted"] as Int == 1),
                            it["user_id"] as Long
                    )
            )
        }

        return retros
    }

    fun create(name: String, userId: Long): Retro
    {
        val identifier = UUID.randomUUID().toString()
        val insertDescriptor = InsertDescriptor()
        insertDescriptor.table = "Retros"
        insertDescriptor.columns = listOf("identifier", "name", "user_id", "deleted")
        insertDescriptor.values = listOf(identifier, name, userId, 0)

        connection.insert(insertDescriptor)

        return Retro(Retro.ID(identifier), name, false, userId)
    }

    fun retro(identifier: Retro.ID): Retro?
    {
        val selectDescriptor = SelectDescriptor()
        selectDescriptor.table = "Retros"
        selectDescriptor.where = Equal("identifier", identifier.rawValue)

        val result  = connection.select(selectDescriptor)

        return result.firstOrNull()?.let {
            Retro(
                    Retro.ID(it["identifier"] as String),
                    it["name"] as String,
                    (it["deleted"] as Int == 1),
                    it["user_id"] as Long
            )
        }
    }

    fun delete(identifier: Retro.ID)
    {
        val updateDescriptor = UpdateDescriptor()
        updateDescriptor.table = "Retros"
        updateDescriptor.where = Equal("identifier", identifier.rawValue)
        updateDescriptor.values = mapOf("deleted" to 1)
        connection.update(updateDescriptor)
    }

    fun join(userId: Long, identifier: Retro.ID)
    {
        val insertDescriptor = InsertDescriptor()
        insertDescriptor.table = "Retros_Users"
        insertDescriptor.columns = listOf("retro_identifier", "user_id")
        insertDescriptor.values = listOf(identifier.rawValue, userId)

        connection.insert(insertDescriptor)
    }
}
