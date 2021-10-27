package com.luggsoft.k4.v2.sources.segments

data class CommentTagSegment(
    override val content: String,
    override val location: Location,
) : TagSegmentBase()
