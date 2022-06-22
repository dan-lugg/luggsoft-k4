package com.luggsoft.k4.core.sources

import com.luggsoft.k4.core.sources.iterators.DefaultSourceIterator
import com.luggsoft.k4.core.sources.iterators.SourceIterator
import java.io.File
import java.io.InputStream
import java.io.Reader
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
    /*
    fun getLineNumber(index: Int): Int
    */

    /**
     * Attempts to determine the column number (1-based) of the provided character [index].
     *
     * This is implementation dependent. If the implementation is unable to determine the column number, it should return -1.
     */
    /*
    fun getColumnNumber(index: Int): Int
    */

    /**
     * Creates a single-use [SourceIterator][com.luggsoft.k4.v5.SourceIterator] of the underlying character stream for parsing.
     */
    fun createSourceIterator(): SourceIterator

    object UnknownSource : SourceBase()
    {
        override val name: String = "<UNKNOWN>"

        override fun createSourceIterator(): DefaultSourceIterator = DefaultSourceIterator(
            charIterator = iterator { }
        )
    }

    companion object
    {
        fun fromFile(
            file: File,
            charset: Charset = Charsets.UTF_8,
        ): Source = FileSource(
            file = file,
            charset = charset,
        )

        fun fromInputStream(
            name: String,
            inputStream: InputStream,
            charset: Charset = Charsets.UTF_8,
        ): Source = InputStreamSource(
            name = name,
            inputStream = inputStream,
            charset = charset,
        )

        fun fromReader(
            name: String,
            reader: Reader,
        ): Source = ReaderSource(
            name = name,
            reader = reader,
        )

        fun fromString(
            name: String,
            content: String,
        ): Source = StringSource(
            name = name,
            content = content,
        )
    }
}
