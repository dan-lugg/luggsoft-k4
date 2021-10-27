package com.luggsoft.k4.core.templates

import com.luggsoft.k4.core.sources.segments.SegmentList

interface TemplateGenerator
{
    fun generateTemplate(segmentList: SegmentList): Template
}
