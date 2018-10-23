package com.denis.morozov.retrobot.core.flow

import com.denis.morozov.retrobot.core.Storable
import com.denis.morozov.retrobot.core.command.Command
import com.denis.morozov.retrobot.core.command.CommandHandler
import com.denis.morozov.retrobot.bot.data.FlowsStorage
import com.denis.morozov.retrobot.core.database.Database
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

class FlowController(private val database: Database,
                     private val userId: Long,
                     private val commandsHandlers: List<CommandHandler>) {

    private data class State(
            val command: Command,
            val container: Storable.Container
    )

    private var flow: Flow? = null
    private var command: Command? = null

    fun start(command: Command) {
        val commandHandler = commandsHandlers.singleOrNull { it.command == command }
        if (commandHandler != null) {
            flow = commandHandler.flowAssembly.makeFlow()
            this.command = command
        }
    }

    fun restore() {
        database.connect().use { connection ->
            val flowState = FlowsStorage(connection).flow(userId)
            val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

            if (flowState != null) {
                val state = moshi.adapter(State::class.java).fromJson(flowState)
                if (state != null) {
                    val commandHandler = commandsHandlers.singleOrNull { it.command == state.command }
                    if (commandHandler != null) {
                        flow = commandHandler.flowAssembly.makeFlow()
                        flow?.restore(state.container)
                        command = state.command
                    }
                }
            }
        }
    }

    fun handleUpdate(text: String): Flow.Result {
        val flow = flow
        return if (flow != null) {
            flow.handleUpdate(userId, text)
        } else {
            Flow.Result(true, "Unexpected Error", null)
        }
    }

    fun store() {
        val command = command
        val flow = flow
        if (command != null && flow != null) {
            val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
            val flowState = moshi.adapter(State::class.java).toJson(State(command, flow.store()))
            database.connect().use {
                FlowsStorage(it).storeFlow(flowState, userId)
            }
        }
    }

    fun clear() {
        database.connect().use {
            FlowsStorage(it).storeFlow(null, userId)
        }
    }
}
