package com.luggsoft.k4.core

import com.luggsoft.k4.core.sources.Source
import com.luggsoft.k4.core.templates.Template
import org.slf4j.Logger
import java.io.Writer

interface Engine
{
    fun compile(source: Source): Template<Any>

    fun execute(source: Source, model: Any, writer: Writer, logger: Logger)

    fun execute(source: Source, modelProvider: ModelProvider, modelName: String, writer: Writer, logger: Logger)
}
