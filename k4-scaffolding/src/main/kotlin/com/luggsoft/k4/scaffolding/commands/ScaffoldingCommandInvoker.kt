package com.luggsoft.k4.scaffolding.commands

import com.luggsoft.common.commands.CommandInvokerBase

class ScaffoldingCommandInvoker(
    commandHandlerProvider: ScaffoldingCommandHandlerProvider
) : CommandInvokerBase<ScaffoldingCommandContext>(
    commandHandlerProvider = commandHandlerProvider
)
