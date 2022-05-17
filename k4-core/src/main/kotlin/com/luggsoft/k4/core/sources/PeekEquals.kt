package com.luggsoft.k4.core

import com.luggsoft.k4.core.sources.SourceIterator

fun SourceIterator.peekEquals(input: CharSequence): Boolean = this.peek(input.length) == input
