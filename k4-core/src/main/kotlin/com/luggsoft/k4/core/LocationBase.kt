package com.luggsoft.k4.core

abstract class LocationBase : Location
{
    final override val startCoordinate: String
        get() = "${this.source.name}:${this.startLineNumber}:${this.startColumnNumber}"

    final override val untilCoordinate: String
        get() = "${this.source.name}:${this.untilLineNumber}:${this.untilColumnNumber}"

    final override fun toString(): String = "${this.startCoordinate}/${this.untilCoordinate}/${this.startIndex}:${this.untilIndex}"
}
