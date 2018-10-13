package com.denis.morozov.retrobot.database.update

import com.denis.morozov.retrobot.database.condition.ConditionDescriptor

class UpdateDescriptor {
    var table: String? = null
    var values: Map<String, Any>? = null
    var where: ConditionDescriptor? = null
}
