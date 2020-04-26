package com.luggsoft.k4.scaffolding.nodes

class TemplateFileNode(
    override val name: String,
    val model: Any?,
    val template: String
) : Node
