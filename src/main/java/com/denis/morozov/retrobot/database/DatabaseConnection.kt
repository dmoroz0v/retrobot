package com.denis.morozov.retrobot.database

import com.denis.morozov.retrobot.database.insert.*
import com.denis.morozov.retrobot.database.delete.*
import java.sql.DriverManager

class DatabaseConnection(connectionString: String): AutoCloseable
{
    private val connection = DriverManager.getConnection(connectionString)

    fun insert(descriptor: InsertDescriptor): Boolean
    {
        return update(InsertSqlBuilder(descriptor))
    }

    fun delete(descriptor: DeleteDescriptor): Boolean
    {
        return update(DeleteSqlBuilder(descriptor))
    }

    private fun update(sqlBuilder: SqlBuilder): Boolean
    {
        val updateStatement = UpdateStatement(connection, sqlBuilder)
        return updateStatement.execute()
    }

    override fun close() {
        connection.close()
    }
}
