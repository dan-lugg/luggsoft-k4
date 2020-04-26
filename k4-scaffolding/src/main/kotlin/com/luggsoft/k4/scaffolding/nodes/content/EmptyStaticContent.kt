package com.luggsoft.k4.scaffolding.nodes.content

import com.luggsoft.k4.scaffolding.nodes.content.StaticContent
import java.io.OutputStream

object EmptyStaticContent : StaticContent
{
    override fun write(outputStream: OutputStream)
    {
        // TODO: noop
    }
}
