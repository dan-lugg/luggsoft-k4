package com.luggsoft.k4.cli.commands

import com.luggsoft.k4.cli.helpers.SourceDeterminer
import com.luggsoft.k4.core.vx.EngineFacade
import org.springframework.stereotype.Component
import picocli.CommandLine
import picocli.CommandLine.Command as PicoCommand
import picocli.CommandLine.Option as PicoOption

@Component
@PicoCommand(name = "execute")
class ExecuteCommand(
    private val engineFacade: EngineFacade,
    private val sourceDeterminer: SourceDeterminer,
) : Command
{
    @PicoOption(
        names = ["-s", "--source"],
        required = true,
    )
    lateinit var sourceDescriptor: String

    @PicoOption(
        names = ["-t", "--target"],
        required = true,
    )
    lateinit var targetDescriptor: String

    override fun call(): Int
    {
        val source = this.sourceDeterminer.determineSource(this.sourceDescriptor)
        val unit = this.engineFacade.execute(source)

        TODO("something meaningful")
    }
}
