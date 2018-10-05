package com.denis.morozov.retrobot

import com.denis.morozov.retrobot.database.*
import com.denis.morozov.retrobot.database.condition.Equal
import com.denis.morozov.retrobot.database.condition.Relationship
import com.denis.morozov.retrobot.database.delete.DeleteDescriptor
import com.denis.morozov.retrobot.database.insert.*
import com.denis.morozov.retrobot.database.join.JoinDescriptor
import com.denis.morozov.retrobot.database.select.SelectDescriptor
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
        val user_id = request.queryParameters["user_id"]
        if (name != null && user_id != null)
        {
            DatabaseConnection(connectionString).use {
                val insertDescriptor = InsertDescriptor()
                insertDescriptor.table = "Retros"
                insertDescriptor.columns = listOf("identifier", "name", "user_id")
                insertDescriptor.values = listOf(UUID.randomUUID().toString(), name, user_id)
                it.insert(insertDescriptor)
            }
        }
        return request.createResponseBuilder(HttpStatus.OK).build()
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
        try {
            return DatabaseConnection(connectionString).use {
                val selectDescriptor = SelectDescriptor()
                selectDescriptor.table = "Retros"
                val joinDescriptor = JoinDescriptor()
                joinDescriptor.type = JoinDescriptor.Type.LEFT
                joinDescriptor.table = "Messages"
                joinDescriptor.on = Relationship("Retros.identifier", "Messages.retro_identifier")
                selectDescriptor.joins = listOf(joinDescriptor)
                val result  = it.select(selectDescriptor)

                val resultString = if (result.isEmpty())
                {
                    "Empty"
                }
                else
                {
                    val titles = result[0].map { it.key }.joinToString(" | ")
                    val valuesString = result
                            .map {
                                it.map { "${it.value}" }.joinToString(" | ")
                            }
                            .joinToString("\n")
                    titles + "\n" + valuesString
                }

                request.createResponseBuilder(HttpStatus.OK).body(resultString).build()
            }
        }
        catch (e: Exception) {
            return request.createResponseBuilder(HttpStatus.INTERNAL_SERVER_ERROR).build()
        }
    }
}
