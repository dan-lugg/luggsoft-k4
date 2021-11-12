package com.luggsoft.k4.core

import com.luggsoft.k4.core.sources.DefaultSourceParser
import com.luggsoft.k4.core.sources.Source
import com.luggsoft.k4.core.sources.SourceParser
import com.luggsoft.k4.core.templates.DefaultTemplateGenerator
import com.luggsoft.k4.core.templates.Template
import com.luggsoft.k4.core.templates.TemplateGenerator
import org.slf4j.Logger
import java.io.Writer

class DefaultEngine(
    private val sourceParser: SourceParser,
    private val templateGenerator: TemplateGenerator,
) : Engine
{
    override fun compile(source: Source): Template<Any>
    {
        val segments = this.sourceParser.parseSource(source)
        return this.templateGenerator.generateTemplate(segments)
    }

    override fun execute(source: Source, model: Any, writer: Writer, logger: Logger)
    {
        val template = this.compile(source)
        template.execute(model, writer, logger)
    }

    override fun execute(source: Source, modelProvider: ModelProvider, modelName: String, writer: Writer, logger: Logger)
    {
        val template = this.compile(source)
        val model = modelProvider.getModel(modelName, template.modelKClass)
        template.execute(model, writer, logger)
    }

    object Instance : Engine by DefaultEngine(
        sourceParser = DefaultSourceParser.Instance,
        templateGenerator = DefaultTemplateGenerator.Instance,
    )
}
