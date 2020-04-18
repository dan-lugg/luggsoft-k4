package com.luggsoft.common.commands

import kotlin.reflect.KClass

interface CommandHandlerProvider
{
    fun getCommandHandler(commandClass: KClass<out Command<*>>): CommandHandler<Command<*>, *>
}
