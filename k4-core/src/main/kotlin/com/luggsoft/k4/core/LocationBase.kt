package com.luggsoft.k4.core

abstract class LocationBase : Location
{
    final override val startLineNumber: Int
        get() = this.source.getLineNumber(this.startIndex)

    final override val untilLineNumber: Int
        get() = this.source.getLineNumber(this.untilIndex)

    final override val startColumnNumber: Int
        get() = this.source.getColumnNumber(this.startIndex)

    final override val untilColumnNumber: Int
        get() = this.source.getColumnNumber(this.untilIndex)

    final override val startCoordinate: String
        get() = "${this.source.name}:${this.startLineNumber}:${this.startColumnNumber}"

    final override val untilCoordinate: String
        get() = "${this.source.name}:${this.untilLineNumber}:${this.untilColumnNumber}"

    final override fun toString(): String = "${this.startCoordinate}/${this.untilCoordinate}"
}
