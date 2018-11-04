package com.denis.morozov.retrobot.core.telegram

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import khttp.post

class Telegram(private val token: String) {

    private val API_ENDPOINT = "https://api.telegram.org/bot"
    private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    fun parseUpdate(json: String): Update? {
        return moshi.adapter(Update::class.java).fromJson(json)
    }

    fun sendMessage(messege: SendMessage) {
        val sendMessageAdapter = moshi.adapter(SendMessage::class.java)
        post(API_ENDPOINT + token + "/sendMessage", json = sendMessageAdapter.toJsonValue(messege))
    }
}
