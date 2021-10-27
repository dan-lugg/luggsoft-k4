package com.luggsoft.k4.core

import com.luggsoft.k4.core.sources.Source
import com.luggsoft.k4.core.templates.Template

interface Engine
{
    fun compile(source: Source): Template

    fun execute(template: Template, model: Any?, templateWriter: Any, templateLogger: Any)
}
