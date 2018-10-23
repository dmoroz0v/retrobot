package com.denis.morozov.retrobot.core.database.delete

import com.denis.morozov.retrobot.core.database.condition.ConditionSqlBuilder
import com.denis.morozov.retrobot.core.database.SqlBuilder

class DeleteSqlBuilder(private val descriptor: DeleteDescriptor) : SqlBuilder
{
    private val conditionSqlBuilder = descriptor.where?.let { ConditionSqlBuilder(it) }

    override val sql: String
        get() {
            val table = descriptor.table

            val placesForValues = conditionSqlBuilder?.sql

            return "DELETE FROM $table WHERE ($placesForValues)"
        }

    override val values: Iterable<Any>? = conditionSqlBuilder?.values
}
