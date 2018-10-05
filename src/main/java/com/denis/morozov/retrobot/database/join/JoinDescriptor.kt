package com.denis.morozov.retrobot.database.join

import com.denis.morozov.retrobot.database.condition.ConditionDescriptor

class JoinDescriptor
{
    enum class Type(val value: String)
    {
        INNER("INNER"), LEFT("LEFT"), RIGHT("RIGHT")
    }

    var type = Type.INNER
    var table: String? = null
    var on: ConditionDescriptor? = null
}
