package com.denis.morozov.retrobot.azure

import com.denis.morozov.retrobot.bot.data.*
import com.denis.morozov.retrobot.core.database.*
import com.microsoft.azure.functions.*
import com.microsoft.azure.functions.annotation.*

class DatabaseEndpoints
{
    private val connectionString = ""

    @FunctionName("createRetro")
    fun createRetro(@HttpTrigger(name = "req", methods = [HttpMethod.GET], authLevel = AuthorizationLevel.ADMIN)
                    request: HttpRequestMessage<String>,
                    context: ExecutionContext): HttpResponseMessage
    {
        context.logger.info("httpMethod " + request.httpMethod)
        val name = request.queryParameters["name"]
        val userId = request.queryParameters["user_id"]?.toLongOrNull()
        val result = if (name != null && userId != null) {
            try {
                DatabaseConnection(connectionString).use {
                    RetrosStorage(it).create(name, userId)
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
    fun deleteRetro(@HttpTrigger(name = "req", methods = [HttpMethod.GET], authLevel = AuthorizationLevel.ADMIN)
                    request: HttpRequestMessage<String>,
                    context: ExecutionContext): HttpResponseMessage
    {
        context.logger.info("httpMethod " + request.httpMethod)
        val identifier = request.queryParameters["identifier"]?.toRetroID()
        val result = if (identifier != null) {
            try {
                DatabaseConnection(connectionString).use {
                    RetrosStorage(it).delete(identifier)
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
    fun selectRetros(@HttpTrigger(name = "req", methods = [HttpMethod.GET], authLevel = AuthorizationLevel.ADMIN)
                     request: HttpRequestMessage<String>,
                     context: ExecutionContext): HttpResponseMessage
    {
        context.logger.info("httpMethod " + request.httpMethod)

        val userId = request.queryParameters["user_id"]?.toLongOrNull()

        val result = if (userId != null) {
            try {
                DatabaseConnection(connectionString).use {
                    val result = RetrosStorage(it).retros(userId)

                    if (result.isEmpty()) {
                        "Empty"
                    } else {
                        result.map {
                            "${it.identifier.rawValue} | ${it.name} | ${it.deleted} | ${it.userId}"
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
    fun selectRetro(@HttpTrigger(name = "req", methods = [HttpMethod.GET], authLevel = AuthorizationLevel.ADMIN)
                    request: HttpRequestMessage<String>,
                    context: ExecutionContext): HttpResponseMessage
    {
        context.logger.info("httpMethod " + request.httpMethod)

        val identifier = request.queryParameters["identifier"]?.toRetroID()

        val result = if (identifier != null) {
            try {
                DatabaseConnection(connectionString).use {
                    val retro = RetrosStorage(it).retro(identifier)

                    if (retro != null) {
                        "${retro.identifier.rawValue} | ${retro.name} | ${retro.deleted} | ${retro.userId}"
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

    @FunctionName("selectMessages")
    fun selectMessages(@HttpTrigger(name = "req", methods = [HttpMethod.GET], authLevel = AuthorizationLevel.ADMIN)
                       request: HttpRequestMessage<String>,
                       context: ExecutionContext): HttpResponseMessage
    {
        context.logger.info("httpMethod " + request.httpMethod)

        val retroIdentifier = request.queryParameters["retro_identifier"]?.toRetroID()
        val messageUserId = request.queryParameters["message_user_id"]?.toLongOrNull()
        val retroUserId = request.queryParameters["retro_user_id"]?.toLongOrNull()

        val result = if (retroIdentifier != null) {
            try {
                DatabaseConnection(connectionString).use {
                    val result = MessagesStorage(it).messages(retroIdentifier, messageUserId, retroUserId)

                    if (result.isEmpty()) {
                        "Empty"
                    } else {
                        result.map {
                            "${it.identifier.rawValue} | ${it.text}"
                        }.joinToString("\n")
                    }
                }
            } catch (e: Exception) {
                e.message
            }
        } else {
            "reference retro_identifier is required. message_user_id is optional. retro_user_id is optional"
        }

        return request.createResponseBuilder(HttpStatus.OK).body(result).build()
    }

    @FunctionName("createMessage")
    fun createMessage(@HttpTrigger(name = "req", methods = [HttpMethod.GET], authLevel = AuthorizationLevel.ADMIN)
                      request: HttpRequestMessage<String>,
                      context: ExecutionContext): HttpResponseMessage
    {
        context.logger.info("httpMethod " + request.httpMethod)
        val text = request.queryParameters["text"]
        val retroIdentifier = request.queryParameters["retro_identifier"]?.toRetroID()
        val userId = request.queryParameters["user_id"]?.toLongOrNull()
        val result = if (text != null && retroIdentifier != null && userId != null) {
            try {
                DatabaseConnection(connectionString).use {
                    val userRetros = RetrosStorage(it).retros(userId)
                    if (userRetros.firstOrNull { it.identifier == retroIdentifier } != null) {
                        MessagesStorage(it).create(text, retroIdentifier, userId)
                        "OK"
                    } else {
                        "User not joined to retro"
                    }
                }
            }
            catch (e: Exception) {
                e.message
            }
        } else {
            "reference text and retro_identifier and user_id"
        }

        return request.createResponseBuilder(HttpStatus.OK).body(result).build()
    }

    @FunctionName("joinUser")
    fun joinUser(@HttpTrigger(name = "req", methods = [HttpMethod.GET], authLevel = AuthorizationLevel.ADMIN)
                 request: HttpRequestMessage<String>,
                 context: ExecutionContext): HttpResponseMessage
    {
        context.logger.info("httpMethod " + request.httpMethod)
        val identifier = request.queryParameters["identifier"]?.toRetroID()
        val userId = request.queryParameters["user_id"]?.toLongOrNull()
        val result = if (identifier != null && userId != null) {
            try {
                DatabaseConnection(connectionString).use {
                    RetrosStorage(it).join(userId, identifier)
                    "OK"
                }
            }
            catch (e: Exception) {
                e.message
            }
        } else {
            "reference identifier and user_id"
        }

        return request.createResponseBuilder(HttpStatus.OK).body(result).build()
    }
}
