package com.luggsoft.k4.core.vx.io

abstract class SourceBase : Source
{
    override val length: Int
        get() = this.content.length

    override fun get(index: Int): Char = this.content[index]

    override fun subSequence(startIndex: Int, endIndex: Int): CharSequence = this.content
        .subSequence(startIndex, endIndex)
}
