package com.luggsoft.k4.core.sources

import com.luggsoft.k4.core.sources.segments.SegmentList

interface SourceParser
{
    fun parseSource(source: Source): SegmentList
}
