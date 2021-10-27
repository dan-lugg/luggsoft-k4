package com.luggsoft.k4.core.vx.util

fun <T> Iterable<T>.isIntersect(other: Iterable<T>): Boolean = this.intersect(other).isNotEmpty()
