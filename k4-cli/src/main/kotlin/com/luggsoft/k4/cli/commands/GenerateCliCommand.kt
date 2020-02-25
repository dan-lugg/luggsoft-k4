package com.luggsoft.k4.cli.commands

import picocli.CommandLine.Command

@Command(
    name = "generate"
)
class GenerateCliCommand : CliCommand
{
    override fun call(): Int
    {
        return 0
    }
}
