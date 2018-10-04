package com.denis.morozov.retrobot.database

import java.sql.Connection

class UpdateStatement(private val connection: Connection,
                      private val sqlBuilder: SqlBuilder)
{
    fun execute(): Boolean
    {
        return connection.prepareStatement(sqlBuilder.sql).use { statement ->

            sqlBuilder.values?.let { values ->

                val iterator = values.iterator()
                var index = 1
                while (iterator.hasNext())
                {
                    statement.setObject(index, iterator.next())
                    ++index
                }
            }

            statement.execute()
        }
    }
}
