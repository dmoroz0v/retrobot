package com.denis.morozov.retrobot

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

    @FunctionName("create")
    fun create(@HttpTrigger(name = "req", methods = arrayOf(HttpMethod.GET), authLevel = AuthorizationLevel.ANONYMOUS)
               request: HttpRequestMessage<String>,
               context: ExecutionContext): HttpResponseMessage
    {
        val name = request.queryParameters["name"]
        if (name != null)
        {
            DatabaseConnection(connectionString).use {
                val insertDescriptor = InsertDescriptor()
                insertDescriptor.table = "Retros"
                insertDescriptor.columns = listOf("identifier", "name", "user_id")
                insertDescriptor.values = listOf(UUID.randomUUID().toString(), name, 1)
                it.insert(insertDescriptor)
            }
        }
        return request.createResponseBuilder(HttpStatus.OK).build()
    }

    @FunctionName("delete")
    fun delete(@HttpTrigger(name = "req", methods = arrayOf(HttpMethod.GET), authLevel = AuthorizationLevel.ANONYMOUS)
               request: HttpRequestMessage<String>,
               context: ExecutionContext): HttpResponseMessage
    {
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
}
