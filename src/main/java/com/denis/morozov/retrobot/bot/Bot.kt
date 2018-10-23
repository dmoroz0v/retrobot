package com.denis.morozov.retrobot.bot

import com.denis.morozov.retrobot.telegram.ReplyKeyboardHide
import com.denis.morozov.retrobot.telegram.SendMessage
import com.denis.morozov.retrobot.core.command.Command
import com.denis.morozov.retrobot.core.command.CommandHandler
import com.denis.morozov.retrobot.core.database.Database
import com.denis.morozov.retrobot.core.flow.FlowController
import com.denis.morozov.retrobot.bot.flows.messages.addmessage.AddMessageFlowAssembly
import com.denis.morozov.retrobot.bot.flows.messages.messages.MessagesFlowAssembly
import com.denis.morozov.retrobot.bot.flows.retros.closeretro.CloseRetroFlowAssembly
import com.denis.morozov.retrobot.bot.flows.retros.createretro.CreateRetroFlowAssembly
import com.denis.morozov.retrobot.bot.flows.retros.joinretro.JoinRetroFlowAssembly
import com.denis.morozov.retrobot.bot.flows.retros.myretros.MyRetrosFlowAssembly
import com.denis.morozov.retrobot.core.flow.services.canceloperation.CancelOperationFlowAssembly

class Bot(connectionString: String) {

    private val commandsHandlers: List<CommandHandler>
    private val database = Database(connectionString)

    init {
        commandsHandlers = listOf(
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
                        CancelOperationFlowAssembly())
        )
    }

    fun update(chatId: Long, userId: Long, text: String): SendMessage {

        val flowController = FlowController(database, userId, commandsHandlers)

        if (text.startsWith("/")) {
            flowController.start(Command(text))
        } else {
            flowController.restore()
        }

        val result = flowController.handleUpdate(text)

        if (result.finished) {
            flowController.clear()
        } else {
            flowController.store()
        }

        return SendMessage(chatId, result.text, result.keyboard
                ?: ReplyKeyboardHide(true))
    }
}
