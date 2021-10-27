package com.luggsoft.k4.core

import com.luggsoft.k4.core.sources.Source
import java.io.Writer

fun Engine.compileAndExecute(source: Source, model: Any?, templateWriter: Any, templateLogger: Any)
{
    val template = this.compile(source)
    this.execute(
        template = template,
        model = model,
        templateWriter = templateWriter,
        templateLogger = templateLogger
    )
}

fun Engine.compileAndExecute(source: Source, model: Any?, writer: Writer)
{
    val template = this.compile(source)
    this.execute(
        template = template,
        model = model,
        writer = writer,
    )
}
