package com.luggsoft.k4.cli.commands

import org.springframework.stereotype.Component
import picocli.CommandLine

@Component
@CommandLine.Command(
    name = "k4",
    subcommands = [
        CompileCommand::class,
        ExecuteCommand::class,
    ],
    showAtFileInUsageHelp = true,
)
class K4Command : Command
{
    override fun call(): Int = 0
}
