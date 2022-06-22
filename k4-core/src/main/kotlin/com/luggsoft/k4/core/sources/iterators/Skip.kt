package com.luggsoft.k4.core.sources.iterators

fun SourceIterator.skip(length: Int): Int
{
    this.next(length)
    return this.index
}
