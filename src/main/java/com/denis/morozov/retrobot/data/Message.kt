package com.denis.morozov.retrobot.data

data class Message(val identifier: ID,
                   val text: String) {
    data class ID(val rawValue: String)
}

fun String.toMessageID(): Message.ID {
    return Message.ID(this)
}
