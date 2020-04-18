package com.luggsoft.common.commands

interface CommandHandler<TCommand : Command<TResult>, TResult : Any?>
{
    fun handleCommand(command: TCommand): TResult
}
