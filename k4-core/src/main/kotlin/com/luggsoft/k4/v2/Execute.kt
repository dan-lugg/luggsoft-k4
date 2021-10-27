package com.luggsoft.k4.v2

import com.luggsoft.k4.v2.templates.Template
import com.luggsoft.k4.v2.templates.TemplateLogger
import com.luggsoft.k4.v2.templates.TemplateWriter
import java.io.Writer

fun Engine.execute(template: Template, model: Any?, writer: Writer) = this.execute(
    template = template,
    model = model,
    templateWriter = TemplateWriter.createDefault(writer),
    templateLogger = TemplateLogger.createDefault(template),
)
