package com.luggsoft.k4.scaffolding.commands

import com.luggsoft.common.commands.CommandHandler
import com.luggsoft.common.logger
import java.io.File

class CreateFileCommandHandler : CommandHandler<CreateFileCommand, ScaffoldingCommandContext, File>
{
    override fun handleCommand(command: CreateFileCommand, commandContext: ScaffoldingCommandContext): File
    {
        try
        {
            val file = File(commandContext.rootDirectory, command.path)
            file.setReadable(true)
            file.setWritable(true)
            val bytes = command.contentBytesProvider.getContentBytes()
            file.writeBytes(bytes)
            return file
        }
        catch (exception: Exception)
        {
            ("Failed to create file at path, ${command.path}").also { message ->
                this.logger.error(message, exception)
                throw Exception(message, exception)
            }
        }
    }
}
