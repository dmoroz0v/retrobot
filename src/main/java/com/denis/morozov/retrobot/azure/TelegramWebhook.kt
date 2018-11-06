package com.denis.morozov.retrobot.azure

import com.denis.morozov.retrobot.bot.BotAssembly
import com.microsoft.azure.functions.annotation.*
import com.microsoft.azure.functions.*

class TelegramWebhook
{
    private val TOKEN = ""
    private val CONNECTION_STRING = ""

    @FunctionName("webhook")
    fun webhook(@HttpTrigger(name = "req", methods = arrayOf(HttpMethod.POST), authLevel = AuthorizationLevel.FUNCTION)
                request: HttpRequestMessage<String>,
                context: ExecutionContext): HttpResponseMessage
    {
        try {
            val bot = BotAssembly(TOKEN, CONNECTION_STRING).bot()
            bot.update(request.body)
        } catch (e: Exception) { }

        return request.createResponseBuilder(HttpStatus.OK).build()
    }
}
