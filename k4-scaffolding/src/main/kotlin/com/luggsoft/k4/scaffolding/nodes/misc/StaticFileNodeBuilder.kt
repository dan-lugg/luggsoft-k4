package com.luggsoft.k4.scaffolding.nodes.misc

import com.luggsoft.k4.scaffolding.nodes.StaticFileNode
import com.luggsoft.k4.scaffolding.nodes.content.BinaryStaticContent
import com.luggsoft.k4.scaffolding.nodes.content.StaticContent
import com.luggsoft.k4.scaffolding.nodes.content.EmptyStaticContent
import com.luggsoft.k4.scaffolding.nodes.content.TextStaticContent
import java.io.File
import java.nio.charset.Charset

class StaticFileNodeBuilder(
    private var name: String,
    private var staticContent: StaticContent = EmptyStaticContent
) : NodeBuilder<StaticFileNode>
{
    fun withText(text: String, charset: Charset = Charsets.UTF_8): StaticFileNodeBuilder
    {
        this.staticContent = TextStaticContent(
            text = text,
            charset = charset
        )
        return this
    }

    fun withText(file: File, charset: Charset = Charsets.UTF_8): StaticFileNodeBuilder
    {
        this.staticContent = TextStaticContent(
            text = file.readText(charset),
            charset = charset
        )
        return this
    }

    fun withBytes(bytes: ByteArray): StaticFileNodeBuilder
    {
        this.staticContent = BinaryStaticContent(
            bytes = bytes
        )
        return this
    }

    fun withBytes(file: File): StaticFileNodeBuilder
    {
        this.staticContent = BinaryStaticContent(
            bytes = file.readBytes()
        )
        return this
    }

    override fun build(): StaticFileNode
    {
        return StaticFileNode(
            name = this.name,
            staticContent = this.staticContent
        )
    }
}
