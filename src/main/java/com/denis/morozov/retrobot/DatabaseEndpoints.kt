package com.denis.morozov.retrobot

import com.denis.morozov.retrobot.database.*
import com.denis.morozov.retrobot.database.Insert.*
import com.microsoft.azure.functions.*
import com.microsoft.azure.functions.annotation.*

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
                val insertStatement = InsertDescriptor()
                insertStatement.table = "Retros"
                insertStatement.columns = arrayOf("identifier", "name", "user_id")
                insertStatement.values = arrayOf("vvvv", name, 22)
                it.insert(insertStatement)
            }
        }
        return request.createResponseBuilder(HttpStatus.OK).build()
    }
}
