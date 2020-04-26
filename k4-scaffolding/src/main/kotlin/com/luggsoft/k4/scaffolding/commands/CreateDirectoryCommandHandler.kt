package com.luggsoft.k4.scaffolding.commands

import com.luggsoft.common.commands.CommandHandler
import java.io.File

class CreateDirectoryCommandHandler : CommandHandler<CreateDirectoryCommand, ScaffoldingCommandContext, File>
{
    override fun handleCommand(command: CreateDirectoryCommand, commandContext: ScaffoldingCommandContext): File
    {
        val directory = File(commandContext.rootDirectory, command.path)
        if (directory.exists() && directory.isDirectory)
        {
            return directory
        }

        if (directory.mkdirs())
        {
            return directory
        }

        throw Exception("Failed to create directory, $directory")
    }
}
