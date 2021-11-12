package com.luggsoft.k4.scaffolding

import com.luggsoft.k4.core.templates.Template

interface TemplateRegistry : Iterable<Map.Entry<String, Template<Any>>>
{
    fun getTemplate(name: String): Template<Any>

    fun putTemplate(name: String, template: Template<Any>)
}
