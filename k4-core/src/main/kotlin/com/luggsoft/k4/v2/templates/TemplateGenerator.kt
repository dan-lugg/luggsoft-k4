package com.luggsoft.k4.v2.templates

import com.luggsoft.k4.v2.sources.segments.SegmentList

interface TemplateGenerator
{
    fun generateTemplate(segmentList: SegmentList): Template
}
