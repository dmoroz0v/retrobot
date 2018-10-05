package com.denis.morozov.retrobot.database.condition

import com.denis.morozov.retrobot.database.SqlBuilder

class ConditionSqlBuilder(private val descriptor: ConditionDescriptor) : SqlBuilder
{
    override val sql: String = toSql(descriptor)

    override val values: Iterable<Any>?
        get() {
            val mutableList: MutableList<Any> = mutableListOf()
            fillList(mutableList, descriptor)
            return mutableList
        }

    private fun toSql(descriptor: ConditionDescriptor): String = when (descriptor)
    {
        is Equal -> "(" + descriptor.field + " = ?)"
        is Relationship -> "(" + descriptor.field1 + " = " + descriptor.field2 + ")"
        is And -> "(" + descriptor.elements.map { toSql(it) }.joinToString(" AND ") + ")"
        is Or -> "(" + descriptor.elements.map { toSql(it) }.joinToString(" OR ") + ")"
        is Not -> "(NOT"  + toSql(descriptor.element) + ")"
    }

    private fun fillList(list: MutableList<Any>, descriptor: ConditionDescriptor)
    {
        when (descriptor)
        {
            is Equal -> list.add(descriptor.value)
            is Relationship -> return
            is And -> descriptor.elements.forEach { fillList(list, it) }
            is Or -> descriptor.elements.forEach { fillList(list, it) }
            is Not -> fillList(list, descriptor.element)
        }
    }
}
