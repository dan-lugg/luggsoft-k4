package com.luggsoft.k4.core.sources.segments

import com.luggsoft.common.text.columnIndexAt
import com.luggsoft.common.text.lineIndexAt
import com.luggsoft.k4.core.sources.Source

abstract class SegmentBase : Segment
{
    fun getStartLineNumber(source: Source): Int = source.content.lineIndexAt(this.location.startIndex) + 1

    fun getUntilLineNumber(source: Source): Int = source.content.lineIndexAt(this.location.untilIndex) + 1

    fun getStartColumnNumber(source: Source): Int = source.content.columnIndexAt(this.location.startIndex) + 1

    fun getUntilColumnNumber(source: Source): Int = source.content.columnIndexAt(this.location.untilIndex) + 1
}
