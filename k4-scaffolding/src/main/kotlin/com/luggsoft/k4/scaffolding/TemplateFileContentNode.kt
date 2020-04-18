package com.luggsoft.k4.scaffolding

class TemplateFileContentNode(
    name: String,
    val model: Any?,
    val template: String
) : FileContentNodeBase(
    name = name
)
