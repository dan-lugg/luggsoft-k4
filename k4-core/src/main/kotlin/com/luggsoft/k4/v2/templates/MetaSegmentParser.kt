package com.luggsoft.k4.v2.templates

import com.luggsoft.k4.v2.sources.segments.MetaTagSegment

interface MetaSegmentParser
{
    fun parseMetaSegments(metaTagSegments: List<MetaTagSegment>): Meta
}
