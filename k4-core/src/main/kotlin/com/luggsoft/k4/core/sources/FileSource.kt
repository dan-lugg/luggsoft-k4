package com.luggsoft.k4.core.sources

import com.luggsoft.common.text.iterator
import com.luggsoft.k4.core.sources.iterators.DefaultSourceIterator
import com.luggsoft.k4.core.sources.iterators.SourceIterator
import java.io.File
import java.nio.charset.Charset

class FileSource(
    private val file: File,
    private val charset: Charset = Charsets.UTF_8,
) : SourceBase()
{
    override val name: String
        get() = this.file.absolutePath

    override fun createSourceIterator(): SourceIterator = DefaultSourceIterator(
        charIterator = this.file.reader(this.charset).iterator(),
    )
}
