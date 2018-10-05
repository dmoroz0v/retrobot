package com.denis.morozov.retrobot.database.select

import com.denis.morozov.retrobot.database.condition.ConditionDescriptor
import com.denis.morozov.retrobot.database.join.JoinDescriptor

class SelectDescriptor
{
    var columns: Iterable<String>? = null
    var table: String? = null
    var joins: Iterable<JoinDescriptor>? = null
    var where: ConditionDescriptor? = null
}
