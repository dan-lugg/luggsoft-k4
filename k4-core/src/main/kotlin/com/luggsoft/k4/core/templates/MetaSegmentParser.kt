package com.luggsoft.k4.core.templates

import com.luggsoft.k4.core.sources.segments.MetaTagSegment

interface MetaSegmentParser
{
    fun parseMetaSegments(metaTagSegments: List<MetaTagSegment>): Meta
}
