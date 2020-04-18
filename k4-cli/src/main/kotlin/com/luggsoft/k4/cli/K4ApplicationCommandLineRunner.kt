package com.luggsoft.k4.cli

import com.luggsoft.k4.cli.commands.Command
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.ExitCodeGenerator
import org.springframework.stereotype.Component
import picocli.CommandLine
import picocli.CommandLine.IFactory

@Component
open class K4ApplicationCommandLineRunner(
    private val command: Command,
    private val factory: IFactory
) : CommandLineRunner, ExitCodeGenerator
{
    private var exitCode: Int = 0

    override fun run(vararg args: String?)
    {
        val commandLine = CommandLine(this.command, this.factory)
        this.exitCode = commandLine.execute(*args)
    }

    override fun getExitCode(): Int
    {
        return this.exitCode
    }
}
