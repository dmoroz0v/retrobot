package com.denis.morozov.retrobot.database.condition

sealed class ConditionDescriptor
class Equal(val field: String, val value: Any) : ConditionDescriptor()
class Relationship(val field1: String, val field2: String) : ConditionDescriptor()
class And(vararg val elements: ConditionDescriptor) : ConditionDescriptor()
class Or(vararg val elements: ConditionDescriptor) : ConditionDescriptor()
class Not(val element: ConditionDescriptor) : ConditionDescriptor()
