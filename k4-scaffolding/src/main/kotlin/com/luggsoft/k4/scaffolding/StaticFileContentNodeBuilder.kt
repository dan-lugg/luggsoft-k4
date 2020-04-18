package com.luggsoft.k4.scaffolding

import java.io.File
import java.nio.charset.Charset

class StaticFileContentNodeBuilder(
    name: String,
    private var content: Content = EmptyContent
) : ContentNodeBuilderBase<StaticFileContentNode>(
    name = name
)
{
    fun withText(text: String, charset: Charset = Charsets.UTF_8): StaticFileContentNodeBuilder
    {
        this.content = TextContent(
            text = text,
            charset = charset
        )
        return this
    }

    fun withText(file: File, charset: Charset = Charsets.UTF_8): StaticFileContentNodeBuilder
    {
        this.content = TextContent(
            text = file.readText(charset),
            charset = charset
        )
        return this
    }

    fun withBytes(bytes: ByteArray): StaticFileContentNodeBuilder
    {
        this.content = BinaryContent(
            bytes = bytes
        )
        return this
    }

    fun withBytes(file: File): StaticFileContentNodeBuilder
    {
        this.content = BinaryContent(
            bytes = file.readBytes()
        )
        return this
    }

    override fun build(): StaticFileContentNode
    {
        return StaticFileContentNode(
            name = this.name,
            content = this.content
        )
    }
}
