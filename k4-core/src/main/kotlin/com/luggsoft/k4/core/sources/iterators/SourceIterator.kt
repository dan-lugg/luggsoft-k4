package com.luggsoft.k4.core.sources.iterators

interface SourceIterator : Iterator<Char>
{
    /**
     * The current character index of the underlying source
     */
    val index: Int

    /**
     * Returns the next [length] characters without advancing the index.
     */
    fun peek(length: Int): CharSequence

    /**
     * Returns the next [length] characters.
     */
    fun next(length: Int): CharSequence
}
