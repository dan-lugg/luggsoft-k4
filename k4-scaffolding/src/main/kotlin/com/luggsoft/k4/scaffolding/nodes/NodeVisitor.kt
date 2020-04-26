package com.luggsoft.k4.scaffolding.nodes

interface NodeVisitor<TResult>
{
    fun visitNode(node: Node, ancestorNodes: List<Node> = emptyList()): TResult
}
