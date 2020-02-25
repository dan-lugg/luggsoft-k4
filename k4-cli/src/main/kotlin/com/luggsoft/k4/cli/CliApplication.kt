package com.luggsoft.k4.cli

import com.luggsoft.k4.cli.commands.GenerateCliCommand
import com.luggsoft.k4.cli.commands.GenerateFromBundleCliCommand
import com.luggsoft.k4.cli.commands.GenerateFromFileCliCommand
import picocli.CommandLine
import kotlin.system.exitProcess

object CliApplication
{
    @JvmStatic
    fun main(args: Array<String>)
    {
        val cliCommand = GenerateCliCommand()
        val exitCode = CommandLine(cliCommand)
            .also { commandLine ->
                commandLine.addSubcommand(GenerateFromFileCliCommand())
                commandLine.addSubcommand(GenerateFromBundleCliCommand())
            }
            .execute(*args)
        exitProcess(exitCode)
    }
}
