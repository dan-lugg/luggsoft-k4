package com.luggsoft.k4.core.sources

import com.luggsoft.common.text.iterator
import com.luggsoft.k4.core.sources.iterators.DefaultSourceIterator
import com.luggsoft.k4.core.sources.iterators.SourceIterator
import java.io.Reader

class ReaderSource(
    override val name: String,
    private val reader: Reader,
) : SourceBase()
{
    override fun createSourceIterator(): SourceIterator = DefaultSourceIterator(
        charIterator = this.reader.iterator(),
    )
}
