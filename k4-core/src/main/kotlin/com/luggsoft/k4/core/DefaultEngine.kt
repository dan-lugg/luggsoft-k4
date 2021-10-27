package com.luggsoft.k4.core

import com.luggsoft.k4.core.sources.DefaultSourceParser
import com.luggsoft.k4.core.sources.Source
import com.luggsoft.k4.core.sources.SourceParser
import com.luggsoft.k4.core.templates.DefaultTemplateGenerator
import com.luggsoft.k4.core.templates.Template
import com.luggsoft.k4.core.templates.TemplateGenerator

class DefaultEngine(
    private val sourceParser: SourceParser,
    private val templateGenerator: TemplateGenerator,
) : Engine
{
    override fun compile(source: Source): Template
    {
        val segments = this.sourceParser.parseSource(source)
        return this.templateGenerator.generateTemplate(segments)
    }

    override fun execute(template: Template, model: Any?, templateWriter: Any, templateLogger: Any)
    {
        template.execute(
            model = model,
            templateWriter = templateWriter,
            templateLogger = templateLogger,
        )
    }

    object Instance : Engine by DefaultEngine(
        sourceParser = DefaultSourceParser.Instance,
        templateGenerator = DefaultTemplateGenerator.Instance,
    )
}
