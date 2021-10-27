package com.luggsoft.k4.core

import com.luggsoft.k4.core.templates.Template
import com.luggsoft.k4.core.templates.TemplateLogger
import com.luggsoft.k4.core.templates.TemplateWriter
import java.io.Writer

fun Engine.execute(template: Template, model: Any?, writer: Writer) = this.execute(
    template = template,
    model = model,
    templateWriter = TemplateWriter.createDefault(writer),
    templateLogger = TemplateLogger.createDefault(template),
)
