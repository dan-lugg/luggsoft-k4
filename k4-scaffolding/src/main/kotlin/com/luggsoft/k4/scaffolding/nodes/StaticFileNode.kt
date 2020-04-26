package com.luggsoft.k4.scaffolding.nodes

import com.luggsoft.k4.scaffolding.nodes.content.StaticContent

class StaticFileNode(
    override val name: String,
    val staticContent: StaticContent
) : Node
