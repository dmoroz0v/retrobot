package com.denis.morozov.retrobot.commands

abstract class Command {
    abstract val key: String
    abstract val description: String
    abstract fun execute(userId: Long,
                         params: Array<String>): CommandResult
}

sealed class CommandResult {
    class Error(val message: String): CommandResult()
    class Success(val result: String): CommandResult()
}
