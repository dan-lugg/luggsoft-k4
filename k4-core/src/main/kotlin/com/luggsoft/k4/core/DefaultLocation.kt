package com.luggsoft.k4.core

import com.luggsoft.k4.core.sources.Source

data class DefaultLocation(
    override val source: Source,
    override val startIndex: Int,
    override val untilIndex: Int,
) : LocationBase()
