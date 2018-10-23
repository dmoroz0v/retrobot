package com.denis.morozov.retrobot.bot.flows.retros.createretro

import com.denis.morozov.retrobot.core.Storable

class CreateRetroContext(var name: String? = null): Storable {

    override fun store(): Storable.Container? {
        val container = Storable.Container()
        container.setString(name, "name")
        return container
    }

    override fun restore(container: Storable.Container) {
        name = container.stringValue("name")
    }
}
