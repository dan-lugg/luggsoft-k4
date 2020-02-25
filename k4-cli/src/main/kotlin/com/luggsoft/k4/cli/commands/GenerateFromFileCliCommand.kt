package com.luggsoft.k4.cli.commands

import picocli.CommandLine.Command
import picocli.CommandLine.Option
import java.io.File

@Command(
    name = "from-file"
)
class GenerateFromFileCliCommand : CliCommand
{
    @Option(
        names = ["-f", "--file"],
        description = [
            "A template filename"
        ]
    )
    var file: File? = null

    override fun call(): Int
    {
        println("$this: file = ${this.file}")
        return 0
    }
}
