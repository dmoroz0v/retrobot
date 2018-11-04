package com.denis.morozov.retrobot.bot

import com.denis.morozov.retrobot.bot.flows.messages.addmessage.AddMessageFlowAssembly
import com.denis.morozov.retrobot.bot.flows.messages.messages.MessagesFlowAssembly
import com.denis.morozov.retrobot.bot.flows.retros.closeretro.CloseRetroFlowAssembly
import com.denis.morozov.retrobot.bot.flows.retros.createretro.CreateRetroFlowAssembly
import com.denis.morozov.retrobot.bot.flows.retros.joinretro.JoinRetroFlowAssembly
import com.denis.morozov.retrobot.bot.flows.retros.myretros.MyRetrosFlowAssembly
import com.denis.morozov.retrobot.bot.flows.retros.shareretro.ShareRetroFlowAssembly
import com.denis.morozov.retrobot.core.Bot
import com.denis.morozov.retrobot.core.command.Command
import com.denis.morozov.retrobot.core.command.CommandHandler
import com.denis.morozov.retrobot.core.database.Database
import com.denis.morozov.retrobot.core.flow.services.canceloperation.CancelOperationFlowAssembly

class BotAssembly(private val token: String,
                  private val connectionString: String) {

    fun bot(): Bot {
        val database = Database(connectionString)
        val commandsHandlers: List<CommandHandler> = listOf(
                CommandHandler(Command("/myretros"),
                        "Print all you created and joined retros",
                        MyRetrosFlowAssembly(database)),
                CommandHandler(Command("/joinretro"),
                        "Join to retro",
                        JoinRetroFlowAssembly(database)),
                CommandHandler(Command("/createretro"),
                        "Create new retro",
                        CreateRetroFlowAssembly(database)),
                CommandHandler(Command("/closeretro"),
                        "Close retro",
                        CloseRetroFlowAssembly(database)),
                CommandHandler(Command("/mymessages"),
                        "You retro messages",
                        MessagesFlowAssembly(database, false)),
                CommandHandler(Command("/allmessages"),
                        "You created retro all messages",
                        MessagesFlowAssembly(database, true)),
                CommandHandler(Command("/addmessage"),
                        "Add message to retro",
                        AddMessageFlowAssembly(database)),
                CommandHandler(Command("/cancel"),
                        "Cancel current operation",
                        CancelOperationFlowAssembly()),
                CommandHandler(Command("/shareretro"),
                        "Share retro",
                        ShareRetroFlowAssembly(database))
        )

        return Bot(token, database, commandsHandlers)
    }
}
