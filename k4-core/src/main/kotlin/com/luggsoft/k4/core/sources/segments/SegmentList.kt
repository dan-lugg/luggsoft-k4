package com.luggsoft.k4.core.sources.segments

import com.luggsoft.k4.core.sources.Source

data class SegmentList(
    val source: Source,
    val segments: List<Segment>,
) : Iterable<Segment> by segments
{
    val bodySegments: List<Segment>
        get() = this.segments.filterNot { segment -> segment is MetaTagSegment }

    val metaTagSegments: List<MetaTagSegment>
        get() = this.segments.filterIsInstance<MetaTagSegment>()

}
