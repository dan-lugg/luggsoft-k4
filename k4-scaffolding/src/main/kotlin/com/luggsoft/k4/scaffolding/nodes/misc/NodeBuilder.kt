package com.luggsoft.k4.scaffolding.nodes.misc

import com.luggsoft.k4.scaffolding.nodes.Node

interface NodeBuilder<TNode : Node>
{
    fun build(): TNode
}
