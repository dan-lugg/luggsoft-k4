package com.luggsoft.k4.core

import com.luggsoft.k4.core.sources.Source

interface Location
{
    /**
     * The location source.
     */
    val source: Source

    /**
     * The starting (inclusive) character index.
     */
    val startIndex: Int

    /**
     * The ending (inclusive) character index.
     */
    val untilIndex: Int

    /**
     * The starting line number.
     *
     * If the [source] implementation is unable to resolve the starting line number, it will be -1.
     *
     * @see [source]
     * @see [Source.getLineNumber]
     */
    val startLineNumber: Int

    /**
     * The ending line number.
     *
     * If the [source] implementation is unable to resolve the ending line number, it will be -1.
     *
     * @see [source]
     * @see [Source.getLineNumber]
     */
    val untilLineNumber: Int

    /**
     * The starting column number.
     *
     * If the [source] implementation is unable to resolve the starting column number, it will be -1.
     *
     * @see [source]
     * @see [Source.getColumnNumber]
     */
    val startColumnNumber: Int

    /**
     * The ending column number.
     *
     * If the [source] implementation is unable to resolve the ending column number, it will be -1.
     *
     * @see [source]
     * @see [Source.getColumnNumber]
     */
    val untilColumnNumber: Int

    /**
     * A string representation of the location's start.
     */
    val startCoordinate: String

    /**
     * A string representation of the location's end.
     */
    val untilCoordinate: String

    object UnknownLocation : LocationBase()
    {
        override val source: Source = Source.UnknownSource
        override val startIndex: Int = -1
        override val untilIndex: Int = -1
        override val startLineNumber: Int = -1
        override val untilLineNumber: Int = -1
        override val startColumnNumber: Int = -1
        override val untilColumnNumber: Int = -1
    }
}
