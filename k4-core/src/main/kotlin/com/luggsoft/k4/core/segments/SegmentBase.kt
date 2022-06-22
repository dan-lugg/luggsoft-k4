package com.luggsoft.k4.core.segments

import com.luggsoft.common.text.kotlinEscape

abstract class SegmentBase : Segment
{
    final override fun toString(): String = """(${this.typeName}@[${this.location}] "${this.content.kotlinEscape()}")"""

    private val typeName: String
        get() = when (this)
        {
            is RawSegment -> "TEXT"
            is CodeTagSegment -> "CODE"
            is EchoTagSegment -> "ECHO"
            is MetaTagSegment -> "META"
            else -> "TODO"
        }

}
