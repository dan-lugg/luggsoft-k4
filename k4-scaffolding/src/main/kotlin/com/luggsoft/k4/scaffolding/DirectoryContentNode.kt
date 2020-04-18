package com.luggsoft.k4.scaffolding

data class DirectoryContentNode(
    override val name: String,
    val childContentNodes: List<ContentNode>
) : ContentNode
