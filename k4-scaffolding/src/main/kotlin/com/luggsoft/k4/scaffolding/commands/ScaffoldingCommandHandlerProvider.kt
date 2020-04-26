package com.luggsoft.k4.scaffolding.commands

import com.luggsoft.common.commands.Command
import com.luggsoft.common.commands.CommandHandlerMapping
import com.luggsoft.common.commands.CommandHandlerProviderBase

class ScaffoldingCommandHandlerProvider(
    mappings: Iterable<CommandHandlerMapping<Command<*>, ScaffoldingCommandContext, *>>
) : CommandHandlerProviderBase<ScaffoldingCommandContext>(
    mappings = mappings
)
