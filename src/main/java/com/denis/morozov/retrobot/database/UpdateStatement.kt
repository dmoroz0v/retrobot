package com.denis.morozov.retrobot.database

import java.sql.Connection

class UpdateStatement(private val connection: Connection,
                      private val sqlBuilder: SqlBuilder)
{
    fun execute(): Boolean
    {
        return connection.prepareStatement(sqlBuilder.sql).use {
            sqlBuilder.values.forEachIndexed { index, obj ->
                it.setObject(index + 1, obj)
            }
            it.execute()
        }
    }
}
