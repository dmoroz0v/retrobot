package com.denis.morozov.retrobot.core.flow

import com.denis.morozov.retrobot.core.telegram.ReplyKeyboardMarkup

interface FlowInputHandler {

    data class InputMarkup(
            val texts: List<String>,
            val keyboard: ReplyKeyboardMarkup? = null,
            val interrupt: Boolean = false) {

        constructor(text: String,
                    keyboard: ReplyKeyboardMarkup? = null,
                    interrupt: Boolean = false) : this(listOf(text), keyboard, interrupt)
    }

    fun inputMarkup(userId: Long): InputMarkup
    fun handle(userId: Long, text: String)
}
