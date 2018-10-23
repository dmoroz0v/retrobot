package com.denis.morozov.retrobot.bot.flows.messages.addmessage

import com.denis.morozov.retrobot.core.Storable
import com.denis.morozov.retrobot.bot.data.Retro

class AddMessageContext(var retroId: Retro.ID? = null,
                        var text: String? = null): Storable {

    override fun store(): Storable.Container? {
        val container = Storable.Container()
        container.setString(retroId?.rawValue, "retroId")
        container.setString(text, "text")
        return container
    }

    override fun restore(container: Storable.Container) {
        container.stringValue("retroId")?.let {
            retroId = Retro.ID(it)
        }
        text = container.stringValue("text")
    }
}
