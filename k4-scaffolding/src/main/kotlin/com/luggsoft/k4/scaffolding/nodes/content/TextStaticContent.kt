package com.luggsoft.k4.scaffolding.nodes.content

import java.io.OutputStream
import java.nio.charset.Charset

class TextStaticContent(
    val text: String,
    val charset: Charset = Charsets.UTF_8
) : StaticContent
{
    override fun write(outputStream: OutputStream)
    {
        val writer = outputStream.writer(this.charset)
        writer.write(this.text)
        writer.flush()
    }
}
