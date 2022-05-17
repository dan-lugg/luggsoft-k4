package com.luggsoft.k4.core.segments

import com.luggsoft.k4.core.Location

data class CodeTagSegment(
    override val content: String,
    override val location: Location,
) : TagSegmentBase()
{
    override val typeName: String = "CODE"
}
