package com.luggsoft.k4.scaffolding

interface ContentNodeBuilder<TContentNode : ContentNode>
{
    fun build(): TContentNode
}
