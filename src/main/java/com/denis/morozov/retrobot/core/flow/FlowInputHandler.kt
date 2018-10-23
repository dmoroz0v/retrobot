package com.denis.morozov.retrobot.core.flow

import com.denis.morozov.retrobot.telegram.ReplyKeyboardMarkup

interface FlowInputHandler {

    data class InputMarkup(
            val text: String,
            val keyboard: ReplyKeyboardMarkup? = null,
            val interrupt: Boolean = false
    )

    fun inputMarkup(userId: Long): InputMarkup
    fun handle(userId: Long, text: String)
}
