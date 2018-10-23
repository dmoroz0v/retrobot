package com.denis.morozov.retrobot.core.database.update

import com.denis.morozov.retrobot.core.database.condition.ConditionDescriptor

class UpdateDescriptor {
    var table: String? = null
    var values: Map<String, Any>? = null
    var where: ConditionDescriptor? = null
}
