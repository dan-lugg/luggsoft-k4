package com.luggsoft.k4.core.segments

import com.luggsoft.common.text.kotlinEscape

abstract class SegmentBase : Segment
{
    protected abstract val typeName: String

    final override fun toString(): String = "(${this.typeName}@${this.location}: ${this.content.kotlinEscape()})"
}
