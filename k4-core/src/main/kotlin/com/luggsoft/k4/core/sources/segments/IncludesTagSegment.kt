package com.luggsoft.k4.core.sources.segments

data class IncludesTagSegment(
    override val content: String,
    override val location: Location,
): TagSegmentBase()
