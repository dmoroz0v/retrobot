package com.denis.morozov.retrobot

import com.beust.klaxon.Klaxon
import com.microsoft.azure.functions.annotation.*
import com.microsoft.azure.functions.*
import khttp.post

class TelegramWebhook
{
    private val API_ENDPOINT = "https://api.telegram.org/bot"
    private val TOKEN = ""

    @FunctionName("webhook")
    fun webhook(@HttpTrigger(name = "req", methods = arrayOf(HttpMethod.POST), authLevel = AuthorizationLevel.ANONYMOUS)
                request: HttpRequestMessage<String>,
                context: ExecutionContext): HttpResponseMessage
    {
        val message = (Klaxon().parse<Update>(request.body))?.message

        if (message != null)
        {
            val payload = mapOf("chat_id" to message.chat.id, "text" to message.text)
            post(API_ENDPOINT + TOKEN + "/sendMessage", json = payload)
        }

        return request.createResponseBuilder(HttpStatus.OK).build()
    }
}
