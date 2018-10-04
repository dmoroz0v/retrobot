package com.denis.morozov.retrobot.database.condition

sealed class ConditionDescriptor
class Equal(val name: String, val value: Any) : ConditionDescriptor()
class And(vararg val elements: ConditionDescriptor) : ConditionDescriptor()
class Or(vararg val elements: ConditionDescriptor) : ConditionDescriptor()
class Not(val element: ConditionDescriptor) : ConditionDescriptor()