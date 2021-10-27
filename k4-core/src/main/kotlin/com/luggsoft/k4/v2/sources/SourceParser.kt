package com.luggsoft.k4.v2.sources

import com.luggsoft.k4.v2.sources.segments.SegmentList

interface SourceParser
{
    fun parseSource(source: Source): SegmentList
}
