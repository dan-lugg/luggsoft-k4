package com.luggsoft.k4.cli

import com.luggsoft.k4.cli.commands.GenerateCallable
import picocli.CommandLine
import kotlin.system.exitProcess

object CliApplication
{
    @JvmStatic
    fun main(args: Array<String>)
    {
        val callable = GenerateCallable()
        val exitCode = CommandLine(callable).execute(*args)
        exitProcess(exitCode)
    }
}
