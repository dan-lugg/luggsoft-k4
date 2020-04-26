package com.luggsoft.k4.scaffolding.commands

import com.luggsoft.common.commands.CommandHandler
import java.io.InputStream
import java.time.Instant
import java.util.*
import java.util.concurrent.CompletableFuture

class ShellInvokeCommandHandler : CommandHandler<ShellInvokeCommand, ScaffoldingCommandContext, Int>
{
    override fun handleCommand(command: ShellInvokeCommand, commandContext: ScaffoldingCommandContext): Int
    {
        val processBuilder = ProcessBuilder().also { processBuilder ->
            processBuilder.command(command.parts)
            processBuilder.directory(commandContext.rootDirectory)
        }

        val process = processBuilder.start()
        val processFuture = CompletableFuture.supplyAsync { process.waitFor() }
        val outLoggingFuture = this.createLoggingFuture(process.inputStream, "OUT")
        val errLoggingFuture = this.createLoggingFuture(process.errorStream, "ERR")

        return CompletableFuture.allOf(processFuture, outLoggingFuture, errLoggingFuture)
            .thenApply { _ -> processFuture.get() }
            .join()
    }

    private fun createLoggingFuture(inputStream: InputStream, prefix: String): CompletableFuture<*>
    {
        return CompletableFuture.runAsync {
            val scanner = Scanner(inputStream)
            while (scanner.hasNextLine())
            {
                val line = scanner.nextLine()
                println("OUT: ${Instant.now()} > $line")
            }
        }
    }
}
