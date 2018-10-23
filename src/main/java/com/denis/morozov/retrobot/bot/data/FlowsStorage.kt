package com.denis.morozov.retrobot.bot.data

import com.denis.morozov.retrobot.core.database.DatabaseConnection
import com.denis.morozov.retrobot.core.database.condition.Equal
import com.denis.morozov.retrobot.core.database.delete.DeleteDescriptor
import com.denis.morozov.retrobot.core.database.insert.InsertDescriptor
import com.denis.morozov.retrobot.core.database.select.SelectDescriptor

class FlowsStorage(private val connection: DatabaseConnection) {

    fun storeFlow(value: String?, userId: Long) {

        val deleteDescriptor = DeleteDescriptor()
        deleteDescriptor.table = "FlowsStore"
        deleteDescriptor.where = Equal("user_id", userId)
        connection.delete(deleteDescriptor)

        if (value != null) {
            val insertDescriptor = InsertDescriptor()
            insertDescriptor.table = "FlowsStore"
            insertDescriptor.columns = listOf("user_id", "text")
            insertDescriptor.values = listOf(userId, value)

            connection.insert(insertDescriptor)
        }
    }

    fun flow(userId: Long): String? {
        val selectDescriptor = SelectDescriptor()
        selectDescriptor.columns = listOf("*")
        selectDescriptor.table = "FlowsStore"
        selectDescriptor.where = Equal("user_id", userId)

        val result  = connection.select(selectDescriptor)

        if (result.isNotEmpty()) {
            return result.first()["text"] as? String
        }

        return null
    }
}
