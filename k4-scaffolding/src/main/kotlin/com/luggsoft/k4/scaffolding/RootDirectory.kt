package com.luggsoft.k4.scaffolding

fun rootDirectory(block: DirectoryContentNodeBuilder.() -> Unit): DirectoryContentNode
{
    return DirectoryContentNodeBuilder(":").also(block).build()
}
