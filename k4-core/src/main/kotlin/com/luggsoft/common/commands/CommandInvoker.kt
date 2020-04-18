package com.luggsoft.common.commands

interface CommandInvoker
{
    fun invokeCommand(command: Command<*>): Any?
}
