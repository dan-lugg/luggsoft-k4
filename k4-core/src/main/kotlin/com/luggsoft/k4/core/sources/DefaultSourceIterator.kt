package com.luggsoft.k4.core.sources

class DefaultSourceIterator(
    private val charIterator: Iterator<Char>,
) : SourceIterator
{
    override var index: Int = 0
        private set

    private val peekBuffer: MutableList<Char> = mutableListOf()

    override fun peek(length: Int): CharSequence
    {
        while (this.charIterator.hasNext() && this.peekBuffer.size < length)
        {
            this.peekBuffer.add(this.charIterator.next())
        }

        return this.peekBuffer.take(length).toCharArray().concatToString()
    }

    override fun next(length: Int): CharSequence
    {
        val charBuffer = mutableListOf<Char>()

        while (this.hasNext() && charBuffer.size < length)
        {
            charBuffer.add(this.next())
        }

        return charBuffer.toCharArray().concatToString()
    }

    override fun hasNext(): Boolean = this.peekBuffer.isNotEmpty() || this.charIterator.hasNext()

    override fun next(): Char
    {
        var char = this.peekBuffer.removeFirstOrNull()

        if (char == null)
        {
            char = this.charIterator.next()
        }

        this.index += 1
        return char
    }
}
