package com.luggsoft.k4.core

import com.luggsoft.k4.core.sources.SourceIterator

fun SourceIterator.nextEquals(input: CharSequence): Boolean = this.next(input.length) == input
