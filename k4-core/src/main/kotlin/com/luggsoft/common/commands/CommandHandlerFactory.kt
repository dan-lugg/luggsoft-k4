package com.luggsoft.common.commands

class CommandHandlerFactory<TCommand : Command<TResult>, TResult : Any?>(
    private val block: () -> CommandHandler<TCommand, TResult>
)
{
    fun createCommandHandler(): CommandHandler<TCommand, TResult>
    {
        return this.block.invoke()
    }
}
