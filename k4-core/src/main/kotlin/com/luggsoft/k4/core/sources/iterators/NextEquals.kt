package com.luggsoft.k4.core.sources.iterators

fun SourceIterator.nextEquals(input: CharSequence): Boolean = this.next(input.length) == input
