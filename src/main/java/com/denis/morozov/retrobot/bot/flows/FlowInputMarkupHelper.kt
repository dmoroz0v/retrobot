package com.denis.morozov.retrobot.bot.flows

import com.denis.morozov.retrobot.core.telegram.KeyboardButton
import com.denis.morozov.retrobot.core.telegram.ReplyKeyboardMarkup
import com.denis.morozov.retrobot.bot.data.Retro
import com.denis.morozov.retrobot.bot.data.RetrosStorage
import com.denis.morozov.retrobot.core.database.Database

class FlowInputMarkupHelper {
    companion object {
        fun makeRetrosKeyboard(database: Database, userId: Long, onlyMy: Boolean = false): ReplyKeyboardMarkup? {
            return database.connect().use {
                val retros = RetrosStorage(it).retros(userId)

                if (retros.isEmpty()) {
                    null
                } else {
                    val buttons = retros.filter { !onlyMy || (it.userId == userId && onlyMy) }.map {
                        listOf(KeyboardButton(it.name + " - " + it.identifier.rawValue))
                    }
                    ReplyKeyboardMarkup(buttons, true, true)
                }
            }
        }

        fun retroID(text: String): Retro.ID? {
            return text.splitToSequence(" - ").lastOrNull()?.let {
                return Retro.ID(it)
            }
        }
    }
}
