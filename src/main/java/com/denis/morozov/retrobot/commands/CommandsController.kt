package com.denis.morozov.retrobot.commands

class CommandsController {
    private val commands: MutableMap<String, Command> = mutableMapOf()

    fun setCommand(command: Command) {
        commands[command.key] = command
    }

    fun execute(userId: Long, commandLine: String): CommandResult {

        val params = commandLine.split(" ").map { it.trim() }.filter { it.isNotEmpty() }

        val commandKey = params.firstOrNull()
        if (commandKey != null) {

            val command = commands[commandKey]
            if (command != null) {
                return command.execute(userId, params.drop(1).toTypedArray())
            }
        }

        return buildHelp()
    }

    private fun buildHelp(): CommandResult {
        val result = commands.map {
            it.key + " - " + it.value.description
        }.joinToString("\n")

        return CommandResult.Success(result)
    }
}
