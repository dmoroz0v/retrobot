package com.denis.morozov.retrobot.bot.flows.messages.addmessage

import com.denis.morozov.retrobot.core.Storable
import com.denis.morozov.retrobot.bot.flows.ChoiceRetroContext

class AddMessageContext(var text: String? = null): Storable {

    val choiceRetroContext = ChoiceRetroContext()

    override fun store(): Storable.Container? {
        val container = Storable.Container()
        container.setString(text, "text")

        choiceRetroContext.store()?.let {
            container.setContainer(it, "choiceRetroContext")
        }

        return container
    }

    override fun restore(container: Storable.Container) {
        text = container.stringValue("text")

        container.container("choiceRetroContext")?.let {
            choiceRetroContext.restore(it)
        }
    }
}
