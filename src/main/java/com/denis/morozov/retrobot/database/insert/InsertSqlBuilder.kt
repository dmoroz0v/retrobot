package com.denis.morozov.retrobot.database.insert

import com.denis.morozov.retrobot.database.SqlBuilder

class InsertSqlBuilder(private val descriptor: InsertDescriptor) : SqlBuilder
{
    override val sql: String
        get() {
            val table = descriptor.table
            val columns = descriptor.columns?.joinToString(separator = ",")

            val placesForValues = descriptor.values
                    ?.map { "?" }
                    ?.joinToString(separator = ", ")

            return "INSERT INTO $table ($columns) VALUES ($placesForValues)"
        }

    override val values: Iterable<Any>? = descriptor.values
}
