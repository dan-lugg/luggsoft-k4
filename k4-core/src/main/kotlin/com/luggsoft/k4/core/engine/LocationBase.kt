package com.luggsoft.k4.core.engine

abstract class LocationBase(
    final override val name: String,
    final override val startIndex: Int,
    final override val untilIndex: Int
) : Location
