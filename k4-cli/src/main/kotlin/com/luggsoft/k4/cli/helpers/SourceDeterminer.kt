package com.luggsoft.k4.cli.helpers

import com.luggsoft.k4.core.vx.io.Source

interface SourceDeterminer
{
    fun determineSource(descriptor: String): Source
}
