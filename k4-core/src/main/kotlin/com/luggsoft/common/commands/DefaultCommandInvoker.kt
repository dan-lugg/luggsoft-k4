package com.luggsoft.common.commands

import com.luggsoft.common.logger

class DefaultCommandInvoker(
    private val commandHandlerProvider: CommandHandlerProvider
) : CommandInvoker
{
    @Throws(CommandInvokerException::class)
    override fun invokeCommand(command: Command<*>): Any?
    {
        try
        {
            return this.commandHandlerProvider
                .getCommandHandler(command::class)
                .handleCommand(command)
        }
        catch (exception: Exception)
        {
            ("Failed to invoke command").also { message ->
                this.logger.error(message, exception)
                throw CommandInvokerException(message, exception)
            }
        }
    }
}
