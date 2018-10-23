package com.denis.morozov.retrobot.bot.data

data class Retro(val identifier: ID,
                 val name: String,
                 val deleted: Boolean,
                 val userId: Long) {
    data class ID(val rawValue: String)
}

fun String.toRetroID(): Retro.ID {
    return Retro.ID(this)
}
