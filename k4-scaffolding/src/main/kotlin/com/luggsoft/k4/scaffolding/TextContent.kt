package com.luggsoft.k4.scaffolding

import java.io.OutputStream
import java.nio.charset.Charset

class TextContent(
    val text: String,
    val charset: Charset = Charsets.UTF_8
) : Content
{
    override fun write(outputStream: OutputStream)
    {
        val writer = outputStream.writer(this.charset)
        writer.write(this.text)
        writer.flush()
    }
}
