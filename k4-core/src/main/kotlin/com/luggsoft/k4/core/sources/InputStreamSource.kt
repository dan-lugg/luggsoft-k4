package com.luggsoft.k4.core.sources

import com.luggsoft.common.text.iterator
import com.luggsoft.k4.core.sources.iterators.DefaultSourceIterator
import com.luggsoft.k4.core.sources.iterators.SourceIterator
import java.io.InputStream
import java.nio.charset.Charset

class InputStreamSource(
    override val name: String,
    private val inputStream: InputStream,
    private val charset: Charset = Charsets.UTF_8,
) : SourceBase()
{
    override fun createSourceIterator(): SourceIterator = DefaultSourceIterator(
        charIterator = this.inputStream.reader(this.charset).iterator(),
    )
}

