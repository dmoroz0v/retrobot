package com.denis.morozov.retrobot.database

import com.denis.morozov.retrobot.database.Insert.*
import java.sql.DriverManager

class DatabaseConnection(connectionString: String): AutoCloseable
{
    private val connection = DriverManager.getConnection(connectionString)

    fun insert(descriptor: InsertDescriptor): Boolean
    {
        val sqlBuilder = InsertSqlBuilder(descriptor)
        val updateStatement = UpdateStatement(connection, sqlBuilder)
        return updateStatement.execute()
    }

    override fun close() {
        connection.close()
    }
}
