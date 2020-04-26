package com.luggsoft.k4.scaffolding.nodes

import com.luggsoft.common.EMPTY_STRING
import com.luggsoft.k4.scaffolding.nodes.misc.DirectoryNodeBuilder

fun rootDirectory(block: DirectoryNodeBuilder.() -> Unit): DirectoryNode
{
    return DirectoryNodeBuilder(EMPTY_STRING).also(block).build()
}
