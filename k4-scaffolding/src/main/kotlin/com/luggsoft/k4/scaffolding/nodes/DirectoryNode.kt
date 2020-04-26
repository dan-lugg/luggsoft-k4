package com.luggsoft.k4.scaffolding.nodes

data class DirectoryNode(
    override val name: String,
    val childNodes: List<Node>
) : Node
