package com.luggsoft.k4.v2.sources.segments

data class KotlinTagSegment(
    override val content: String,
    override val location: Location,
) : TagSegmentBase()
