package com.luggsoft.k4.v2

import com.luggsoft.k4.v2.sources.Source
import com.luggsoft.k4.v2.templates.Template

interface Engine
{
    fun compile(source: Source): Template

    fun execute(template: Template, model: Any?, templateWriter: Any, templateLogger: Any)
}
