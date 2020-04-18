package com.luggsoft.k4.scaffolding

import java.io.File
import java.io.InputStream
import java.io.InputStreamReader
import java.nio.charset.Charset

class TemplateFileContentNodeBuilder(
    name: String,
    private var model: Any? = null,
    private var template: String = String()
) : ContentNodeBuilderBase<TemplateFileContentNode>(
    name = name
)
{
    fun withModel(model: Any): TemplateFileContentNodeBuilder
    {
        this.model = model
        return this
    }

    fun withTemplate(code: String): TemplateFileContentNodeBuilder
    {
        this.template = code
        return this
    }

    fun withTemplate(file: File, charset: Charset = Charsets.UTF_8): TemplateFileContentNodeBuilder
    {
        this.template = file.readText(charset)
        return this
    }

    fun withTemplate(inputStream: InputStream, charset: Charset = Charsets.UTF_8): TemplateFileContentNodeBuilder
    {
        inputStream.reader(charset).use { inputStreamReader: InputStreamReader ->
            this.template = inputStreamReader.readText()
        }
        return this
    }


    override fun build(): TemplateFileContentNode
    {
        return TemplateFileContentNode(
            name = this.name,
            model = this.model,
            template = this.template
        )
    }
}
