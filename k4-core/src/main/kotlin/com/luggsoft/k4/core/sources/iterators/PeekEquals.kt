package com.luggsoft.k4.core.sources.iterators

fun SourceIterator.peekEquals(input: CharSequence): Boolean = this.peek(input.length) == input
