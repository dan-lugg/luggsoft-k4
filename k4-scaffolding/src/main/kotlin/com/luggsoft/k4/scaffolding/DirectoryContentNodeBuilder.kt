package com.luggsoft.k4.scaffolding

class DirectoryContentNodeBuilder(
    private val name: String
) : ContentNodeBuilder<DirectoryContentNode>
{
    private val childContentNodes: MutableList<ContentNode> = mutableListOf()

    fun addStaticFile(name: String, block: StaticFileContentNodeBuilder.() -> Unit = {}): DirectoryContentNodeBuilder
    {
        val staticFileContentNodeBuilder = StaticFileContentNodeBuilder(
            name = name
        )
        val staticFileContentNode = staticFileContentNodeBuilder.also(block).build()
        this.childContentNodes.add(staticFileContentNode)
        return this
    }

    fun addTemplateFile(name: String, block: TemplateFileContentNodeBuilder.() -> Unit = {}): DirectoryContentNodeBuilder
    {
        val templateFileContentNodeBuilder = TemplateFileContentNodeBuilder(
            name = name
        )
        val templateFileContentNode = templateFileContentNodeBuilder.also(block).build()
        this.childContentNodes.add(templateFileContentNode)
        return this
    }

    fun addDirectory(name: String, block: DirectoryContentNodeBuilder.() -> Unit = {}): DirectoryContentNodeBuilder
    {
        val directoryContentNode = DirectoryContentNodeBuilder(name).also(block).build()
        this.childContentNodes.add(directoryContentNode)
        return this
    }

    override fun build(): DirectoryContentNode
    {
        return DirectoryContentNode(
            name = this.name,
            childContentNodes = this.childContentNodes
        )
    }
}
