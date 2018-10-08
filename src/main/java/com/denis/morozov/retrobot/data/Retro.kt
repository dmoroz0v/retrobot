package com.denis.morozov.retrobot.data

data class Retro(val identifier: String,
                 val name: String,
                 val deleted: Boolean,
                 val messages: List<Message>)
