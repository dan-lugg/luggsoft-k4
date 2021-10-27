package com.luggsoft.k4.cli.helpers

import com.luggsoft.k4.core.vx.io.Source
import com.luggsoft.k4.core.vx.io.URLSource
import org.springframework.stereotype.Component

@Component
class DefaultSourceDeterminer : SourceDeterminer
{
    override fun determineSource(descriptor: String): Source = URLSource(
        spec = descriptor
    )
}
