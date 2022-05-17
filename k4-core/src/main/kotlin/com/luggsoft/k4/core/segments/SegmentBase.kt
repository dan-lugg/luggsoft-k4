package com.luggsoft.k4.core.segments

import com.luggsoft.k4.core.wrap

abstract class SegmentBase : Segment
{
    protected abstract val typeName: String

    final override fun toString(): String = "(${this.typeName} @ ${this.location}, ${wrap(this.content)})"
}
