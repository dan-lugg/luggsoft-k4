package com.luggsoft.k4.core

import java.io.File
import java.nio.charset.Charset

interface Source
{
    val name: String
    val text: CharSequence

    companion object
    {
        @JvmStatic
        fun fromFile(file: File, charset: Charset = Charsets.UTF_8): Source = DefaultSource(
            name = file.absolutePath,
            text = file.readText(charset)
        )

        @JvmStatic
        fun fromResource(name: String, charset: Charset = Charsets.UTF_8): Source = DefaultSource(
            name = name,
            text = this::class.java.getResourceAsStream(name).reader(charset).readText()
        )

        @JvmStatic
        fun fromString(name: String, text: String): Source = DefaultSource(
            name = name,
            text = text
        )
    }
}
