package com.denis.morozov.retrobot.bot.flows

import com.denis.morozov.retrobot.bot.data.Retro
import com.denis.morozov.retrobot.bot.data.RetrosStorage
import com.denis.morozov.retrobot.core.database.Database
import com.denis.morozov.retrobot.core.flow.*
import com.denis.morozov.retrobot.core.telegram.KeyboardButton
import com.denis.morozov.retrobot.core.telegram.ReplyKeyboardMarkup

class ChoiceRetroInputHandler(private val database: Database,
                              private val context: ChoiceRetroContext,
                              private val onlyMyRetros: Boolean,
                              private val message: String): FlowInputHandler {

    override fun inputMarkup(userId: Long): FlowInputHandler.InputMarkup {
        val retros = retors(database, userId, onlyMyRetros)
        return if (retros != null) {
            context.retros = retros.toMap()
            val keyboard = makeRetrosKeyboard(retros)
            FlowInputHandler.InputMarkup(message, keyboard)
        } else {
            FlowInputHandler.InputMarkup("Retros not found", null, true)
        }
    }

    override fun handle(userId: Long, text: String) {
        context.retroId = context.retros?.get(text)
    }

    private fun retors(database: Database, userId: Long, onlyMy: Boolean = false): List<Pair<String, Retro.ID>>? {
        return database.connect().use {
            var retros = RetrosStorage(it).retros(userId)

            if (retros.isEmpty()) {
                null
            } else {
                val result: MutableList<Pair<String, Retro.ID>> = mutableListOf()
                retros = retros.filter { !onlyMy || (it.userId == userId && onlyMy) }

                retros.forEachIndexed { index, retro ->
                    val pair = Pair((index + 1).toString() + ". " + retro.name, retro.identifier)
                    result.add(pair)
                }
                result
            }
        }
    }

    private fun makeRetrosKeyboard(retros: List<Pair<String, Retro.ID>>): ReplyKeyboardMarkup {
        val buttons = retros.map {
            listOf(KeyboardButton(it.first))
        }
        return ReplyKeyboardMarkup(buttons, true, true)
    }
}
