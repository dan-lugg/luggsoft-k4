package com.luggsoft.k4.core.segments

import com.luggsoft.k4.core.Location

data class RawSegment(
    override val content: String,
    override val location: Location,
) : SegmentBase()
{
    override val typeName: String = "TEXT"
}
