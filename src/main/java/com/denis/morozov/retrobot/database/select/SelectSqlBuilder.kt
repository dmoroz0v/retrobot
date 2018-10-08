package com.denis.morozov.retrobot.database.select

import com.denis.morozov.retrobot.database.SqlBuilder
import com.denis.morozov.retrobot.database.condition.ConditionSqlBuilder
import com.denis.morozov.retrobot.database.join.JoinSqlBuilder

class SelectSqlBuilder(private val descriptor: SelectDescriptor) : SqlBuilder
{
    private val joinsSqlBuilders = descriptor.joins?.let { joins ->
        joins.map { joinDescriptor -> JoinSqlBuilder(joinDescriptor) }
    }

    private val conditionSqlBuilder = descriptor.where?.let { ConditionSqlBuilder(it) }

    override val sql: String
        get() {
            val columns = descriptor.columns?.joinToString(", ") ?: "*"
            val table = descriptor.table
            val joins = joinsSqlBuilders?.map { it.sql }?.joinToString(",") ?: ""
            val where = conditionSqlBuilder?.let { "WHERE " + it.sql } ?: ""

            return "SELECT $columns FROM $table $joins $where"
        }

    override val values: Iterable<Any>?
        get() {
            val result: MutableList<Any> = mutableListOf()

            joinsSqlBuilders?.forEach { joinSqlBuilder ->
                joinSqlBuilder.values?.let {
                    result.addAll(it)
                }
            }

            conditionSqlBuilder?.values?.let {
                result.addAll(it)
            }

            return result
        }
}
