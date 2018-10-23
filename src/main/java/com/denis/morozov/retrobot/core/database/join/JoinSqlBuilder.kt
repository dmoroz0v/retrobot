package com.denis.morozov.retrobot.core.database.join

import com.denis.morozov.retrobot.core.database.SqlBuilder
import com.denis.morozov.retrobot.core.database.condition.ConditionSqlBuilder

class JoinSqlBuilder(private val descriptor: JoinDescriptor) : SqlBuilder
{
    private val conditionSqlBuilder = descriptor.on?.let { ConditionSqlBuilder(it) }

    override val sql: String
        get() {
            val type = descriptor.type.value
            val table = descriptor.table
            val on = conditionSqlBuilder?.sql

            return "$type JOIN $table ON $on"
        }

    override val values: Iterable<Any>? = conditionSqlBuilder?.values
}
