package com.luggsoft.k4.core.templates

import com.luggsoft.k4.core.sources.Source
import java.io.Writer

interface Template
{
    val source: Source

    fun execute(model: Any?, templateWriter: Any, templateLogger: Any)

    fun save(writer: Writer)
}
