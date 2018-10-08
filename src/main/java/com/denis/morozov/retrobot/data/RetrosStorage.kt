package com.denis.morozov.retrobot.data

import com.denis.morozov.retrobot.database.DatabaseConnection
import com.denis.morozov.retrobot.database.condition.Equal
import com.denis.morozov.retrobot.database.condition.Or
import com.denis.morozov.retrobot.database.condition.Relationship
import com.denis.morozov.retrobot.database.join.JoinDescriptor
import com.denis.morozov.retrobot.database.select.SelectDescriptor

class RetrosStorage(private val connection: DatabaseConnection): AutoCloseable
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

    override fun close() {
        connection.close()
    }
}
