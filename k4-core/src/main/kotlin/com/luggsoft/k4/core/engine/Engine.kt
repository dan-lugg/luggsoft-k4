package com.luggsoft.k4.core.engine

import com.luggsoft.k4.core.Source
import com.luggsoft.k4.core.Template
import com.luggsoft.k4.core.engine.generators.Script
import java.io.Writer

interface Engine
{
    fun compileToScript(source: Source): Script

    fun compileToTemplate(source: Source): Template

    fun compileAndExecute(source: Source, writer: Writer, model: Any? = null)
}
