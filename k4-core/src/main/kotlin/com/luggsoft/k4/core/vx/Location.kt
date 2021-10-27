package com.luggsoft.k4.core.vx

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.luggsoft.common.text.columnIndexAt
import com.luggsoft.common.text.lineIndexAt
import com.luggsoft.k4.core.vx.io.Source

@JsonTypeInfo(use = JsonTypeInfo.Id.MINIMAL_CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@type")
data class Location(
    val source: Source,
    val startIndex: Int,
    val untilIndex: Int,
)
{
    @get:JsonIgnore
    val range: IntRange
        get() = this.startIndex..this.untilIndex

    @get:JsonIgnore
    val startLineNumber: Int
        get() = this.source.content.lineIndexAt(this.startIndex) + 1

    @get:JsonIgnore
    val untilLineNumber: Int
        get() = this.source.content.lineIndexAt(this.untilIndex) + 1

    @get:JsonIgnore
    val startColumnNumber: Int
        get() = this.source.content.columnIndexAt(this.startIndex) + 1

    @get:JsonIgnore
    val untilColumnNumber: Int
        get() = this.source.content.columnIndexAt(this.untilIndex) + 1

    fun toStartString(): String = "${this.source.name}:${this.startLineNumber}:${this.startColumnNumber}"

    fun toUntilString(): String = "${this.source.name}:${this.untilLineNumber}:${this.untilColumnNumber}"

    fun toRangeString(): String = "${this.toStartString()}, ${this.toUntilString()}"
}
