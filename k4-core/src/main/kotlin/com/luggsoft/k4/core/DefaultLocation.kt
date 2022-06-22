package com.luggsoft.k4.core

import com.luggsoft.k4.core.sources.Source

data class DefaultLocation(
    override val source: Source,
    override val startIndex: Int,
    override val untilIndex: Int,
    override val startLineNumber: Int = -1,
    override val untilLineNumber: Int = -1,
    override val startColumnNumber: Int = -1,
    override val untilColumnNumber: Int = -1,
) : LocationBase()
