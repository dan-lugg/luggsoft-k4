package com.luggsoft.k4.core.sources

import com.luggsoft.common.text.LineSeparator
import com.luggsoft.common.text.detectLineSeparator
import java.io.File
import java.nio.charset.Charset

interface Source
{
    /**
     * The source name.
     *
     * This is implementation dependent. Examples could be a local filename, a URL, or a user defined identifier.
     */
    val name: String

    /**
     * Attempts to determine the line number (1-based) of the provided character [index].
     *
     * This is implementation dependent. If the implementation is unable to determine the line number, it should return -1.
     */
    fun getLineNumber(index: Int): Int

    /**
     * Attempts to determine the column number (1-based) of the provided character [index].
     *
     * This is implementation dependent. If the implementation is unable to determine the column number, it should return -1.
     */
    fun getColumnNumber(index: Int): Int

    /**
     * Creates a single-use [SourceIterator][com.luggsoft.k4.v5.SourceIterator] of the underlying character stream for parsing.
     */
    fun createSourceIterator(): SourceIterator

    object UnknownSource : SourceBase()
    {
        override val name: String = "<UNKNOWN>"

        override fun getLineNumber(index: Int): Int = -1

        override fun getColumnNumber(index: Int): Int = -1

        override fun createSourceIterator(): DefaultSourceIterator = DefaultSourceIterator(
            charIterator = iterator { }
        )
    }

    companion object
    {
        private data class LineInfo(
            val lineIndex: Int,
            val startIndex: Int,
            val untilIndex: Int,
        )

        fun fromFile(file: File, charset: Charset = Charsets.UTF_8): Source = object : Source
        {
            private val file = file
            private val charset = charset
            private val lineInfos: List<LineInfo>
            private val lineSeparator: LineSeparator = file.detectLineSeparator(charset)

            init
            {
                var totalLength = 0
                this.lineInfos = this.file.readLines(this.charset).withIndex().map { (index, line) ->
                    val lineInfo = LineInfo(
                        lineIndex = index,
                        startIndex = totalLength,
                        untilIndex = totalLength + line.length + this.lineSeparator.charSequence.length,
                    )
                    totalLength += line.length
                    return@map lineInfo
                }
            }

            override val name: String
                get() = this.file.absolutePath

            override fun getLineNumber(index: Int): Int
            {
                return this.lineInfos
                    .first { lineInfo -> lineInfo.startIndex <= index && lineInfo.untilIndex >= index }
                    .lineIndex + 1
            }

            override fun getColumnNumber(index: Int): Int
            {
                val lineInfo = this.lineInfos
                    .first { lineInfo -> lineInfo.startIndex <= index && lineInfo.untilIndex >= index }
                return index - lineInfo.startIndex + 1
            }

            override fun createSourceIterator(): DefaultSourceIterator = DefaultSourceIterator(
                charIterator = this.file.readText(this.charset).iterator()
            )
        }
    }
}
