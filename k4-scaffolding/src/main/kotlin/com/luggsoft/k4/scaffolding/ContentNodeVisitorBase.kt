package com.luggsoft.k4.scaffolding

abstract class ContentNodeVisitorBase<TResult> : ContentNodeVisitor<TResult>
{
    final override fun visitContentNode(contentNode: ContentNode, ancestorContentNodes: List<ContentNode>): TResult
    {
        return when (contentNode)
        {
            is DirectoryContentNode -> this.visitDirectoryContentNode(contentNode, ancestorContentNodes)
            is StaticFileContentNode -> this.visitStaticFileContentNode(contentNode, ancestorContentNodes)
            is TemplateFileContentNode -> this.visitTemplateFileContentNode(contentNode, ancestorContentNodes)
            else -> TODO()
        }
    }

    protected abstract fun visitDirectoryContentNode(directoryContentNode: DirectoryContentNode, ancestorContentNodes: List<ContentNode>): TResult

    protected abstract fun visitStaticFileContentNode(staticFileContentNode: StaticFileContentNode, ancestorContentNodes: List<ContentNode>): TResult

    protected abstract fun visitTemplateFileContentNode(templateFileContentNode: TemplateFileContentNode, ancestorContentNodes: List<ContentNode>): TResult
}
