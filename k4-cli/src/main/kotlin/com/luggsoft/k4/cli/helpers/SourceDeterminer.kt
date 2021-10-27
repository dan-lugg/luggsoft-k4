package com.luggsoft.k4.cli.helpers

import com.luggsoft.k4.core.sources.Source

interface SourceDeterminer
{
    fun determineSource(descriptor: String): Source
}
