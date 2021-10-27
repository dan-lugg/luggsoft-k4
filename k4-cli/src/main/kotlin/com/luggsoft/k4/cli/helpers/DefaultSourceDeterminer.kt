package com.luggsoft.k4.cli.helpers

import com.luggsoft.k4.core.sources.DefaultSource
import com.luggsoft.k4.core.sources.Source
import org.springframework.stereotype.Component
import java.io.File

@Component
class DefaultSourceDeterminer : SourceDeterminer
{
    override fun determineSource(descriptor: String): Source = DefaultSource(
        name = descriptor,
        content = File(descriptor).readText(),
    )
}
