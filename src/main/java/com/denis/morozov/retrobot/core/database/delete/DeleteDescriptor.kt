package com.denis.morozov.retrobot.core.database.delete

import com.denis.morozov.retrobot.core.database.condition.ConditionDescriptor

class DeleteDescriptor
{
    var table: String? = null
    var where: ConditionDescriptor? = null
}
