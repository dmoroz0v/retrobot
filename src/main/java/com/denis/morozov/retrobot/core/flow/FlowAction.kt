package com.denis.morozov.retrobot.core.flow

interface FlowAction
{
    fun execute(userId: Long): String
}
