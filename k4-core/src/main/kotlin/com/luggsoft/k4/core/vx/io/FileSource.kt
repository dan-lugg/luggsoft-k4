package com.luggsoft.k4.core.vx.io

import java.io.File
import java.nio.charset.Charset

class FileSource(
    private val file: File,
    private val charset: Charset = Charsets.UTF_8
) : SourceBase()
{
    constructor(pathname: String, charset: Charset = Charsets.UTF_8) : this(
        file = File(pathname),
        charset = charset,
    )

    override val name: String
        get() = this.file.absolutePath

    override val content: CharSequence by lazy {
        return@lazy this.file.readText(this.charset)
    }
}
