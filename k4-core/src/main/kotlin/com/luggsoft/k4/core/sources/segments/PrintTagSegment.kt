package com.luggsoft.k4.core.sources.segments

data class PrintTagSegment(
    override val content: String,
    override val location: Location,
) : TagSegmentBase()
