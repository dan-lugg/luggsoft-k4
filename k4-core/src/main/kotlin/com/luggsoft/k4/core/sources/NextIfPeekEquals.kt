package com.luggsoft.k4.core

import com.luggsoft.k4.core.sources.SourceIterator

fun SourceIterator.nextIfPeekEquals(input: CharSequence): CharSequence? = if (this.peekEquals(input)) this.next(input.length) else null
