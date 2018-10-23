package com.denis.morozov.retrobot.core.database.select

import com.denis.morozov.retrobot.core.database.condition.ConditionDescriptor
import com.denis.morozov.retrobot.core.database.join.JoinDescriptor

class SelectDescriptor
{
    var columns: Iterable<String>? = null
    var table: String? = null
    var joins: Iterable<JoinDescriptor>? = null
    var where: ConditionDescriptor? = null
}
