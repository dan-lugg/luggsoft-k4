package com.luggsoft.common.commands

import kotlin.reflect.KClass

class CommandHandlerMapping<TCommand : Command<TResult>, TResult : Any?>(
    val commandClass: KClass<out TCommand>,
    val commandHandlerFactory: CommandHandlerFactory<TCommand, TResult>
)
