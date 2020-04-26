package com.luggsoft.k4.scaffolding.nodes.misc

import com.luggsoft.k4.scaffolding.nodes.Node

abstract class NodeBuilderBase<TNode : Node>(
    protected var name: String
) : NodeBuilder<TNode>
