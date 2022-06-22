package com.luggsoft.k4.core.segments.parsers

import com.luggsoft.k4.core.segments.Segment
import com.luggsoft.k4.core.sources.Source

interface SegmentParser
{
    fun parseSegments(source: Source): Iterable<Segment>
}
