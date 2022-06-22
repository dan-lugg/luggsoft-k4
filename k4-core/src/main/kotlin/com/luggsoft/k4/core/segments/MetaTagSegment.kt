package com.luggsoft.k4.core.segments

import com.luggsoft.k4.core.Location

data class MetaTagSegment(
    override val content: String,
    override val location: Location,
) : TagSegmentBase()
