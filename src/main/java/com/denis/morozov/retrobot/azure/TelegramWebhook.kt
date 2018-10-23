package com.denis.morozov.retrobot.azure

import com.denis.morozov.retrobot.bot.BotController
import com.microsoft.azure.functions.annotation.*
import com.microsoft.azure.functions.*

class TelegramWebhook
{
    private val TOKEN = ""
    private val CONNECTION_STRING = ""

    @FunctionName("webhook")
    fun webhook(@HttpTrigger(name = "req", methods = arrayOf(HttpMethod.POST), authLevel = AuthorizationLevel.ANONYMOUS)
                request: HttpRequestMessage<String>,
                context: ExecutionContext): HttpResponseMessage
    {
        try {
            val botController =  BotController(TOKEN, CONNECTION_STRING)
            botController.update(request.body)
        } catch (e: Exception) {
        }

        return request.createResponseBuilder(HttpStatus.OK).build()
    }
}
