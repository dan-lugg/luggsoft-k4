package com.luggsoft.k4.scaffolding

import java.io.OutputStream

class BinaryContent(
    val bytes: ByteArray
) : Content
{
    override fun write(outputStream: OutputStream)
    {
        outputStream.write(this.bytes)
    }
}
