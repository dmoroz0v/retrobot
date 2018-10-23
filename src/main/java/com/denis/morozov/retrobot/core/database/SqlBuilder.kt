package com.denis.morozov.retrobot.core.database

interface SqlBuilder
{
    val sql: String
    val values: Iterable<Any>?
}
