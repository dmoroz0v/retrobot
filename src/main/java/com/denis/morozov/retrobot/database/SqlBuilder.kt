package com.denis.morozov.retrobot.database

interface SqlBuilder
{
    val sql: String
    val values: Iterable<Any>?
}
