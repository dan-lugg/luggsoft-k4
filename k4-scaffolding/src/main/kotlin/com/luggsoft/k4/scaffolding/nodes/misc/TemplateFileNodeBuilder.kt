package com.luggsoft.k4.scaffolding.nodes.misc

import com.luggsoft.k4.scaffolding.nodes.TemplateFileNode
import java.io.File
import java.io.InputStream
import java.io.InputStreamReader
import java.nio.charset.Charset

class TemplateFileNodeBuilder(
    private var name: String,
    private var model: Any? = null,
    private var template: String = String()
) : NodeBuilder<TemplateFileNode>
{
    fun withModel(model: Any): TemplateFileNodeBuilder
    {
        this.model = model
        return this
    }

    fun withTemplate(code: String): TemplateFileNodeBuilder
    {
        this.template = code
        return this
    }

    fun withTemplate(file: File, charset: Charset = Charsets.UTF_8): TemplateFileNodeBuilder
    {
        this.template = file.readText(charset)
        return this
    }

    fun withTemplate(inputStream: InputStream, charset: Charset = Charsets.UTF_8): TemplateFileNodeBuilder
    {
        inputStream.reader(charset).use { inputStreamReader: InputStreamReader ->
            this.template = inputStreamReader.readText()
        }
        return this
    }


    override fun build(): TemplateFileNode
    {
        return TemplateFileNode(
            name = this.name,
            model = this.model,
            template = this.template
        )
    }
}
