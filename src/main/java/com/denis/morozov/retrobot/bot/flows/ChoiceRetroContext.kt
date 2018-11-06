package com.denis.morozov.retrobot.bot.flows

import com.denis.morozov.retrobot.core.Storable
import com.denis.morozov.retrobot.bot.data.Retro

class ChoiceRetroContext(var retroId: Retro.ID? = null,
                         var retros: Map<String, Retro.ID>? = null): Storable {

    override fun store(): Storable.Container? {
        val container = Storable.Container()
        container.setString(retroId?.rawValue, "retroId")
        containerFromRetros(retros)?.let {
            container.setContainer(it, "retros")
        }
        return container
    }

    override fun restore(container: Storable.Container) {
        container.stringValue("retroId")?.let {
            retroId = Retro.ID(it)
        }
        retros = retrosFromContainer(container.container("retros"))
    }

    private fun containerFromRetros(retros: Map<String, Retro.ID>?): Storable.Container? {
        if (retros == null) {
            return null
        }
        val retrosContainer = Storable.Container()
        retros.forEach {
            retrosContainer.setString(it.value.rawValue, it.key)
        }
        return retrosContainer
    }

    private fun retrosFromContainer(container: Storable.Container?): Map<String, Retro.ID>? {
        if (container == null) {
            return null
        }
        val retros: MutableMap<String, Retro.ID> = mutableMapOf()
        container.allStringKyes().forEach { key ->
            val value = container.stringValue(key)
            if (value != null) {
                retros[key] = Retro.ID(value)
            }
        }
        return retros
    }
}
