package com.denis.morozov.retrobot.bot.flows.messages.messages

import com.denis.morozov.retrobot.core.Storable
import com.denis.morozov.retrobot.bot.data.Retro

class MessagesContext(var retroId: Retro.ID? = null): Storable {

    override fun store(): Storable.Container? {
        val container = Storable.Container()
        container.setString(retroId?.rawValue, "retroId")
        return container
    }

    override fun restore(container: Storable.Container) {
        container.stringValue("retroId")?.let {
            retroId = Retro.ID(it)
        }
    }
}
