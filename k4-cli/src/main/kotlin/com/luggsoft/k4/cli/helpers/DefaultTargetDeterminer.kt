package com.luggsoft.k4.cli.helpers

import org.springframework.stereotype.Component

@Component
class DefaultTargetDeterminer : TargetDeterminer
{
    override fun determineTarget(descriptor: String): Target = when (descriptor)
    {
        "" -> StdoutTarget()
        else -> TODO("Unknown descriptor: $descriptor")
    }
}
