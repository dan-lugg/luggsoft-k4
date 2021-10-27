package com.luggsoft.k4.cli.commands

import com.luggsoft.common.EMPTY_STRING
import com.luggsoft.common.logger
import com.luggsoft.k4.cli.helpers.SourceDeterminer
import com.luggsoft.k4.cli.helpers.TargetDeterminer
import com.luggsoft.k4.core.vx.EngineFacade
import org.springframework.stereotype.Component
import picocli.CommandLine.Command as PicoCommand
import picocli.CommandLine.Option as PicoOption

@Component
@PicoCommand(name = "compile")
class CompileCommand(
    private val engineFacade: EngineFacade,
    private val sourceDeterminer: SourceDeterminer,
    private val targetDeterminer: TargetDeterminer,
) : Command
{
    @PicoOption(names = ["-s", "--source"], required = true)
    var sourceDescriptor: String = EMPTY_STRING

    @PicoOption(names = ["-t", "--target"], required = false)
    var targetDescriptor: String = EMPTY_STRING

    override fun call(): Int
    {
        try
        {
            val source = this.sourceDeterminer.determineSource(this.sourceDescriptor)
            val target = this.targetDeterminer.determineTarget(this.targetDescriptor)
            val template = this.engineFacade.compile(source)
            val targetStringBuffer = StringBuffer()
            template.render(
                model = null,
                output = targetStringBuffer,
                logger = this.logger,
            )
            target.write(targetStringBuffer)
            return 0
        } catch (exception: Exception)
        {
            this.logger.error("Whoops", exception)
            return -1
        }
    }
}

