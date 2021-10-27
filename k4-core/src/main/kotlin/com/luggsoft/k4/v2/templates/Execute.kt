package com.luggsoft.k4.v2.templates

import java.io.Writer

fun Template.execute(model: Any?, writer: Writer) = this.execute(
    model = model,
    templateWriter = TemplateWriter.createDefault(writer),
    templateLogger = TemplateLogger.createDefault(this)
)

