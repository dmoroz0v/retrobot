package com.denis.morozov.retrobot.database

import com.denis.morozov.retrobot.database.insert.*
import com.denis.morozov.retrobot.database.delete.*
import com.denis.morozov.retrobot.database.select.SelectDescriptor
import com.denis.morozov.retrobot.database.select.SelectSqlBuilder
import java.sql.DriverManager
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Statement

class DatabaseConnection(connectionString: String): AutoCloseable
{
    private val connection = DriverManager.getConnection(connectionString)

    fun insert(descriptor: InsertDescriptor): Boolean
    {
        return prepareStatement(InsertSqlBuilder(descriptor)).use { it.execute() }
    }

    fun delete(descriptor: DeleteDescriptor): Boolean
    {
        return prepareStatement(DeleteSqlBuilder(descriptor)).use { it.execute() }
    }

    fun select(descriptor: SelectDescriptor): List<Map<String, Any?>>
    {
        return prepareStatement(SelectSqlBuilder(descriptor)).use { statement ->
            statement.executeQuery().use { resultSet ->
                val metaData = resultSet.metaData
                val columnLabels = List<String>(metaData.columnCount) {
                    metaData.getColumnLabel(it + 1)
                }

                val result: MutableList<Map<String, Any?>> = mutableListOf()

                while (resultSet.next())
                {
                    val row: MutableMap<String, Any?> = mutableMapOf()
                    columnLabels.forEach { columnName ->
                        row[columnName] = resultSet.getObject(columnName)
                    }
                    result.add(row)
                }

                result
            }
        }
    }

    private fun prepareStatement(sqlBuilder: SqlBuilder): PreparedStatement
    {
        val statement = connection.prepareStatement(sqlBuilder.sql)
        sqlBuilder.values?.let { values ->
            val iterator = values.iterator()
            var index = 1
            while (iterator.hasNext())
            {
                statement.setObject(index, iterator.next())
                ++index
            }
        }
        return statement
    }

    override fun close() {
        connection.close()
    }
}
