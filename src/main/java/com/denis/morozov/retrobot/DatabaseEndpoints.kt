package com.denis.morozov.retrobot

import com.denis.morozov.retrobot.data.RetrosStorage
import com.denis.morozov.retrobot.database.*
import com.denis.morozov.retrobot.database.condition.Equal
import com.denis.morozov.retrobot.database.delete.DeleteDescriptor
import com.denis.morozov.retrobot.database.insert.*
import com.microsoft.azure.functions.*
import com.microsoft.azure.functions.annotation.*
import java.util.*

class DatabaseEndpoints
{
    private val connectionString = ""

    @FunctionName("createRetro")
    fun createRetro(@HttpTrigger(name = "req", methods = arrayOf(HttpMethod.GET), authLevel = AuthorizationLevel.ANONYMOUS)
                    request: HttpRequestMessage<String>,
                    context: ExecutionContext): HttpResponseMessage
    {
        context.logger.info("httpMethod " + request.httpMethod)
        val name = request.queryParameters["name"]
        val userId = request.queryParameters["user_id"]
        val result = if (name != null && userId != null)
        {
            try {
                RetrosStorage(DatabaseConnection(connectionString)).use {
                    it.create(name, userId)
                    "OK"
                }
            }
            catch (e: Exception) {
                e.message
            }
        } else {
            "reference user_id and name"
        }

        return request.createResponseBuilder(HttpStatus.OK).body(result).build()
    }

    @FunctionName("deleteRetro")
    fun deleteRetro(@HttpTrigger(name = "req", methods = arrayOf(HttpMethod.GET), authLevel = AuthorizationLevel.ANONYMOUS)
                    request: HttpRequestMessage<String>,
                    context: ExecutionContext): HttpResponseMessage
    {
        context.logger.info("httpMethod " + request.httpMethod)
        val identifier = request.queryParameters["identifier"]
        val result = if (identifier != null)
        {
            try {
                RetrosStorage(DatabaseConnection(connectionString)).use {
                    it.delete(identifier)
                    "OK"
                }
            } catch (e: Exception) {
                e.message
            }
        } else {
            "reference identifier"
        }
        return request.createResponseBuilder(HttpStatus.OK).body(result).build()
    }

    @FunctionName("selectRetros")
    fun selectRetros(@HttpTrigger(name = "req", methods = arrayOf(HttpMethod.GET), authLevel = AuthorizationLevel.ANONYMOUS)
                     request: HttpRequestMessage<String>,
                     context: ExecutionContext): HttpResponseMessage
    {
        context.logger.info("httpMethod " + request.httpMethod)

        val userId = request.queryParameters["user_id"]

        val result = if (userId != null) {
            try {
                RetrosStorage(DatabaseConnection(connectionString)).use {
                    val result = it.retros(userId)

                    if (result.isEmpty()) {
                        "Empty"
                    } else {
                        result.map {
                            "${it.identifier} | ${it.name} | ${it.deleted} | ${it.messages.count()}"
                        }.joinToString("\n")
                    }
                }
            } catch (e: Exception) {
                e.message
            }
        } else {
            "reference user_id"
        }

        return request.createResponseBuilder(HttpStatus.OK).body(result).build()
    }

    @FunctionName("selectRetro")
    fun selectRetro(@HttpTrigger(name = "req", methods = arrayOf(HttpMethod.GET), authLevel = AuthorizationLevel.ANONYMOUS)
                    request: HttpRequestMessage<String>,
                    context: ExecutionContext): HttpResponseMessage
    {
        context.logger.info("httpMethod " + request.httpMethod)

        val identifier = request.queryParameters["identifier"]

        val result = if (identifier != null)
        {
            try {
                RetrosStorage(DatabaseConnection(connectionString)).use {
                    val retro = it.retro(identifier)

                    if (retro != null) {
                        "${retro.identifier} | ${retro.name} | ${retro.deleted} | ${retro.messages.count()}"
                    } else {
                        "Empty"
                    }
                }
            } catch (e: Exception) {
                e.message
            }
        } else {
            "reference identifier"
        }

        return request.createResponseBuilder(HttpStatus.OK).body(result).build()
    }
}
