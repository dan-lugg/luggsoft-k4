package com.luggsoft.k4.v2

import com.luggsoft.k4.v2.sources.Source
import com.luggsoft.k4.v2.sources.SourceParser
import com.luggsoft.k4.v2.templates.Template
import com.luggsoft.k4.v2.templates.TemplateGenerator

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

}
