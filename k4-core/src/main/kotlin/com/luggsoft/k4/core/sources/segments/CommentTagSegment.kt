package com.luggsoft.k4.core.sources.segments

data class CommentTagSegment(
    override val content: String,
    override val location: Location,
) : TagSegmentBase()
