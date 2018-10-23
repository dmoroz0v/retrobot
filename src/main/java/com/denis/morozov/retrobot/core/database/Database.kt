package com.denis.morozov.retrobot.core.database

class Database(private val connectionString: String) {

    fun connect(): DatabaseConnection {
        return DatabaseConnection(connectionString)
    }
}
