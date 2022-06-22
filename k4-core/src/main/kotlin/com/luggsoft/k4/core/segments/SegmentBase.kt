package com.luggsoft.k4.core.segments

import com.luggsoft.common.text.kotlinEscape

abstract class SegmentBase : Segment
{
    final override fun toString(): String = """(${this::class.simpleName}@[${this.location}] "${this.content.kotlinEscape()}")"""
}
