package com.denis.morozov.retrobot.core.flow

import com.denis.morozov.retrobot.telegram.ReplyKeyboardMarkup
import com.denis.morozov.retrobot.core.Storable

class Flow(private val inputHandlers: Array<FlowInputHandler>,
           private val action: FlowAction,
           private val context: Storable?): Storable {

    data class Result(
            val finished: Boolean,
            val text: String,
            val keyboard: ReplyKeyboardMarkup? = null
    )

    private var inputStep: Int = -1

    fun handleUpdate(userId: Long, text: String): Result {

        if (inputStep >= 0) {
            inputHandlers[inputStep].handle(userId, text)
        }

        inputStep += 1

        return if (inputStep < inputHandlers.size) {
            val inputMarkup = inputHandlers[inputStep].inputMarkup(userId)
            Result(inputMarkup.interrupt, inputMarkup.text, inputMarkup.keyboard)
        } else {
            Result(true, action.execute(userId), null)
        }
    }

    override fun store(): Storable.Container {
        val container = Storable.Container()

        container.setInt(inputStep, "inputStep")

        context?.store()?.let {
            container.setContainer(it, "context")
        }

        return container
    }

    override fun restore(container: Storable.Container) {
        container.intValue("inputStep")?.let {
            inputStep = it
        }

        container.container("context")?.let {
            context?.restore(it)
        }
    }
}
