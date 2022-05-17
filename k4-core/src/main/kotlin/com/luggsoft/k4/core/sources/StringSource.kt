package com.luggsoft.k4.core.sources

import com.luggsoft.common.text.columnIndexAt
import com.luggsoft.common.text.lineIndexAt

data class StringSource(
    override val name: String,
    val content: String,
) : Source
{
    override fun getLineNumber(index: Int): Int = this.content.lineIndexAt(index) + 1

    override fun getColumnNumber(index: Int): Int = this.content.columnIndexAt(index) + 1

    override fun createSourceIterator(): DefaultSourceIterator = DefaultSourceIterator(
        charIterator = this.content.iterator(),
    )
}
