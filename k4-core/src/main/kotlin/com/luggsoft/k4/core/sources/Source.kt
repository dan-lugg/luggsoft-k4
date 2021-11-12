package com.luggsoft.k4.core.sources

import java.io.File
import java.nio.charset.Charset

interface Source
{
    val name: String
    val content: String

    companion object
    {
        fun fromFile(file: File, charset: Charset = Charsets.UTF_8): Source = DefaultSource(
            name = file.absolutePath,
            content = file.readText(charset),
        )
    }
}
