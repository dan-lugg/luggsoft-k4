package com.luggsoft.k4.scaffolding

abstract class ContentNodeBuilderBase<TContentNode : ContentNode>(
    protected var name: String
) : ContentNodeBuilder<TContentNode>
