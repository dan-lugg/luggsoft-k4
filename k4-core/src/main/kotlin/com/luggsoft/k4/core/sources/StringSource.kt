package com.luggsoft.k4.core.sources

import com.luggsoft.k4.core.sources.iterators.DefaultSourceIterator

data class StringSource(
    override val name: String,
    val content: String,
) : SourceBase()
{
    override fun createSourceIterator(): DefaultSourceIterator = DefaultSourceIterator(
        charIterator = this.content.iterator(),
    )
}
