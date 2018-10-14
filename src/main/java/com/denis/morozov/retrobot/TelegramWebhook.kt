package com.denis.morozov.retrobot

import com.beust.klaxon.Klaxon
import com.denis.morozov.retrobot.commands.CommandResult
import com.denis.morozov.retrobot.commands.CommandsController
import com.denis.morozov.retrobot.commands.retros.*
import com.denis.morozov.retrobot.commands.messages.*
import com.microsoft.azure.functions.annotation.*
import com.microsoft.azure.functions.*
import khttp.post

class TelegramWebhook
{
    private val API_ENDPOINT = "https://api.telegram.org/bot"
    private val TOKEN = ""
    private val connectionString = ""

    @FunctionName("webhook")
    fun webhook(@HttpTrigger(name = "req", methods = arrayOf(HttpMethod.POST), authLevel = AuthorizationLevel.ANONYMOUS)
                request: HttpRequestMessage<String>,
                context: ExecutionContext): HttpResponseMessage
    {
        try {
            val message = (Klaxon().parse<Update>(request.body))?.message

            val chatId = message?.chat?.id
            val userId = message?.from?.id
            val text = message?.text

            if (chatId != null && userId != null && text != null) {

                val commandsController = CommandsController()

                commandsController.setCommand(CreateRetroCommand(connectionString))
                commandsController.setCommand(MyRetrosCommand(connectionString))
                commandsController.setCommand(CloseRetroCommand(connectionString))
                commandsController.setCommand(JoinRetroCommand(connectionString))

                commandsController.setCommand(AddMessageCommand(connectionString))
                commandsController.setCommand(MyMessagesCommand(connectionString))
                commandsController.setCommand(AllMessagesCommand(connectionString))

                val result = commandsController.execute(userId, text)

                val body = when (result) {
                    is CommandResult.Error -> result.message
                    is CommandResult.Success -> result.result
                }

                val payload = mapOf("chat_id" to chatId, "text" to body)
                post(API_ENDPOINT + TOKEN + "/sendMessage", json = payload)
            }
        } catch (e: Exception) {
        }

        return request.createResponseBuilder(HttpStatus.OK).build()
    }
}
