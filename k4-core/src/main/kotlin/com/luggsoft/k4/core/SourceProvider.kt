package com.luggsoft.k4.core

import com.luggsoft.k4.core.io.CharScanner
import java.io.File
import java.nio.charset.Charset

interface SourceProvider
{
    val name: String
    val source: CharSequence
    val sourceCharScanner: CharScanner

    companion object
    {
        @JvmStatic
        fun fromFile(file: File, charset: Charset = Charsets.UTF_8): SourceProvider = DefaultSourceProvider(
            name = file.absolutePath,
            source = file.readText(charset)
        )

        @JvmStatic
        fun fromResource(name: String, charset: Charset = Charsets.UTF_8): SourceProvider = DefaultSourceProvider(
            name = name,
            source = this::class.java.getResourceAsStream(name).reader(charset).readText()
        )
    }
}
