package com.luggsoft.k4.cli.commands

import com.luggsoft.k4.core.util.logger
import picocli.CommandLine.Command
import picocli.CommandLine.Option
import java.io.File
import java.util.concurrent.Callable

@Command(
    name = "generate"
)
class GenerateCallable : Callable<Int>
{
    @Option(
        names = ["-f", "--file"],
        description = [
            "A K4 template filename"
        ]
    )
    lateinit var file: File

    override fun call(): Int
    {
        this.logger.info("file = ${this.file.absolutePath}")
        return 0
    }
}
