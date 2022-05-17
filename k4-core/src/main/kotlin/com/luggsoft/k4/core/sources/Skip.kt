package com.luggsoft.k4.core

import com.luggsoft.k4.core.sources.SourceIterator

fun SourceIterator.skip(length: Int): Int
{
    this.next(length)
    return this.index
}
