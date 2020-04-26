package com.luggsoft.k4.scaffolding.nodes.content

import java.io.OutputStream

interface StaticContent
{
    fun write(outputStream: OutputStream)
}
