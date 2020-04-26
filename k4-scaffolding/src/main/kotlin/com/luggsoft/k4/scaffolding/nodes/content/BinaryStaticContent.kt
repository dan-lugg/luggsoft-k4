package com.luggsoft.k4.scaffolding.nodes.content

import com.luggsoft.k4.scaffolding.nodes.content.StaticContent
import java.io.OutputStream

class BinaryStaticContent(
    val bytes: ByteArray
) : StaticContent
{
    override fun write(outputStream: OutputStream)
    {
        outputStream.write(this.bytes)
    }
}
