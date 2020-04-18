package com.luggsoft.k4.scaffolding

interface ContentNodeVisitor<TResult>
{
    fun visitContentNode(contentNode: ContentNode, ancestorContentNodes: List<ContentNode> = emptyList()): TResult
}
