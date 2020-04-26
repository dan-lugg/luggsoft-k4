package com.luggsoft.k4.scaffolding.nodes.misc

import com.luggsoft.k4.scaffolding.nodes.DirectoryNode
import com.luggsoft.k4.scaffolding.nodes.Node

class DirectoryNodeBuilder(
    private val name: String
) : NodeBuilder<DirectoryNode>
{
    private val childNodes: MutableList<Node> = mutableListOf()

    fun addStaticFile(name: String, block: StaticFileNodeBuilder.() -> Unit = {}): DirectoryNodeBuilder
    {
        val staticFileNode = StaticFileNodeBuilder(name).also(block).build()
        this.childNodes.add(staticFileNode)
        return this
    }

    fun addTemplateFile(name: String, block: TemplateFileNodeBuilder.() -> Unit = {}): DirectoryNodeBuilder
    {
        val templateFileNode = TemplateFileNodeBuilder(name).also(block).build()
        this.childNodes.add(templateFileNode)
        return this
    }

    fun addDirectory(name: String, block: DirectoryNodeBuilder.() -> Unit = {}): DirectoryNodeBuilder
    {
        val directoryNode = DirectoryNodeBuilder(name).also(block).build()
        this.childNodes.add(directoryNode)
        return this
    }

    override fun build(): DirectoryNode
    {
        fun buildRecursive(directoryNames: List<String>): DirectoryNode
        {
            val directoryName = directoryNames.first()
            val childDirectoryNames = directoryNames.drop(1)
            return DirectoryNode(
                name = directoryName,
                childNodes = when (childDirectoryNames.isEmpty())
                {
                    true -> this.childNodes
                    else -> listOf(
                        buildRecursive(
                            directoryNames = childDirectoryNames
                        )
                    )
                }
            )
        }

        return buildRecursive(
            directoryNames = this.name.split('/')
        )
    }
}
