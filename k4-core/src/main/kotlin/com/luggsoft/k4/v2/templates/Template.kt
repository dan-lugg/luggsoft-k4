package com.luggsoft.k4.v2.templates

import com.luggsoft.k4.v2.sources.Source
import java.io.Writer

interface Template
{
    val source: Source

    fun execute(model: Any?, templateWriter: Any, templateLogger: Any)

    fun save(writer: Writer)
}
