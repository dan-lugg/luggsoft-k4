package com.luggsoft.k4.scaffolding

import com.luggsoft.k4.scaffolding.nodes.Node

data class ScaffoldDefinition(
    val name: String,
    val nodes: List<Node>
)
