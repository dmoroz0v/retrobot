package com.denis.morozov.retrobot.core.database.update

import com.denis.morozov.retrobot.core.database.SqlBuilder
import com.denis.morozov.retrobot.core.database.condition.ConditionSqlBuilder

class UpdateSqlBuilder(private val descriptor: UpdateDescriptor) : SqlBuilder
{
    private val conditionSqlBuilder = descriptor.where?.let { ConditionSqlBuilder(it) }

    override val sql: String
        get() {
            val table = descriptor.table
            val setPlaces = descriptor.values
                    ?.map {
                        it.key + " = ?"
                    }
                    ?.joinToString(",")
            val where = conditionSqlBuilder?.let { "WHERE " + it.sql } ?: ""

            return "UPDATE $table SET $setPlaces $where"
        }

    override val values: Iterable<Any>?
        get() {
            val values: MutableList<Any> = mutableListOf()
            descriptor.values?.let { descriptorValues ->
                values.addAll(descriptorValues.map { it.value })
            }
            conditionSqlBuilder?.values?.let {
                values.addAll(it)
            }
            return values
        }
}
