package com.luggsoft.k4.core.sources.segments

data class RawSegment(
    override val content: String,
    override val location: Location,
) : SegmentBase()
