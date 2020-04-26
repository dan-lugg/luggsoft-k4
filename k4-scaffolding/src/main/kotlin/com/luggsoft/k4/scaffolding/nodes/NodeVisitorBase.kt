package com.luggsoft.k4.scaffolding.nodes

abstract class NodeVisitorBase<TResult> : NodeVisitor<TResult>
{
    final override fun visitNode(node: Node, ancestorNodes: List<Node>): TResult
    {
        return when (node)
        {
            is DirectoryNode -> this.visitDirectoryNode(node, ancestorNodes)
            is StaticFileNode -> this.visitStaticFileNode(node, ancestorNodes)
            is TemplateFileNode -> this.visitTemplateFileNode(node, ancestorNodes)
            else -> TODO()
        }
    }

    protected abstract fun visitDirectoryNode(directoryNode: DirectoryNode, ancestorNodes: List<Node>): TResult

    protected abstract fun visitStaticFileNode(staticFileNode: StaticFileNode, ancestorNodes: List<Node>): TResult

    protected abstract fun visitTemplateFileNode(templateFileNode: TemplateFileNode, ancestorNodes: List<Node>): TResult
}
