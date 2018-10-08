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
        if (name != null && userId != null)
        {
            try {
                DatabaseConnection(connectionString).use {
                    val insertDescriptor = InsertDescriptor()
                    insertDescriptor.table = "Retros"
                    insertDescriptor.columns = listOf("identifier", "name", "user_id", "deleted")
                    insertDescriptor.values = listOf(UUID.randomUUID().toString(), name, userId, 0)
                    it.insert(insertDescriptor)
                }
                return request.createResponseBuilder(HttpStatus.OK).body("OK").build()
            }
            catch (e: Exception) {
                return request.createResponseBuilder(HttpStatus.OK).body(e.message).build()
            }
        }
        return request.createResponseBuilder(HttpStatus.OK).body("reference user_id and name").build()
    }

    @FunctionName("deleteRetro")
    fun deleteRetro(@HttpTrigger(name = "req", methods = arrayOf(HttpMethod.GET), authLevel = AuthorizationLevel.ANONYMOUS)
                    request: HttpRequestMessage<String>,
                    context: ExecutionContext): HttpResponseMessage
    {
        context.logger.info("httpMethod " + request.httpMethod)
        val identifier = request.queryParameters["identifier"]
        if (identifier != null)
        {
            DatabaseConnection(connectionString).use {
                val deleteDescriptor = DeleteDescriptor()
                deleteDescriptor.table = "Retros"
                deleteDescriptor.where = Equal("identifier", identifier)
                it.delete(deleteDescriptor)
            }
        }
        return request.createResponseBuilder(HttpStatus.OK).build()
    }

    @FunctionName("selectRetros")
    fun selectRetros(@HttpTrigger(name = "req", methods = arrayOf(HttpMethod.GET), authLevel = AuthorizationLevel.ANONYMOUS)
                     request: HttpRequestMessage<String>,
                     context: ExecutionContext): HttpResponseMessage
    {
        context.logger.info("httpMethod " + request.httpMethod)

        val userId = request.queryParameters["user_id"]

        if (userId != null)
        {
            try {
                return RetrosStorage(DatabaseConnection(connectionString)).use {
                    val result = it.retros(userId)

                    val resultString = if (result.isEmpty()) {
                        "Empty"
                    } else {
                        result.map {
                            "${it.identifier} | ${it.name} | ${it.deleted} | ${it.messages.count()}"
                        }.joinToString("\n")
                    }

                    request.createResponseBuilder(HttpStatus.OK).body(resultString).build()
                }
            } catch (e: Exception) {
                return request.createResponseBuilder(HttpStatus.OK).body(e.message).build()
            }
        }

        return request.createResponseBuilder(HttpStatus.OK).body("reference user_id").build()
    }
}
