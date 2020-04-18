package com.luggsoft.common.commands

import kotlin.reflect.KClass

inline fun <reified TCommand : Command<TResult>, TResult : Any?> commandHandlerMappingOf(commandClass: KClass<TCommand>, commandHandlerFactory: CommandHandlerFactory<TCommand, TResult>): CommandHandlerMapping<Command<*>, *>
{
    val commandHandlerMapping = CommandHandlerMapping(
        commandClass = commandClass,
        commandHandlerFactory = commandHandlerFactory
    )
    return commandHandlerMapping as CommandHandlerMapping<Command<*>, *>
}

inline fun <reified TCommand : Command<TResult>, TResult : Any?> commandHandlerMappingOf(commandHandlerFactory: CommandHandlerFactory<TCommand, TResult>): CommandHandlerMapping<Command<*>, *>
{
    return commandHandlerMappingOf(
        commandClass = TCommand::class,
        commandHandlerFactory = commandHandlerFactory
    )
}

inline fun <reified TCommand : Command<TResult>, TResult : Any?> commandHandlerMappingOf(noinline block: () -> CommandHandler<TCommand, TResult>): CommandHandlerMapping<Command<*>, *>
{
    return commandHandlerMappingOf(
        commandHandlerFactory = CommandHandlerFactory(block)
    )
}
