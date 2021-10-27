package com.luggsoft.k4.core.vx.internal.tokenizers

import com.luggsoft.k4.core.vx.io.Source

abstract class TokenReaderBase : TokenReader
{
    protected abstract val tokenPrefix: String

    protected abstract val tokenSuffix: String

    final override fun readToken(source: Source, startIndex: Int): Token?
    {
        if (!source.content.subSequence(startIndex, source.content.length - 1).startsWith(this.tokenPrefix))
        {
            return null
        }

        var index = startIndex + this.tokenPrefix.length
        val buffer = StringBuilder()

        while (index < source.content.length)
        {
            index = this.consumeRawStringLiteral(
                startIndex = index,
                buffer = buffer,
                source = source,
            )

            index = this.consumeStringLiteral(
                startIndex = index,
                buffer = buffer,
                source = source,
            )

            if (buffer.endsWith(this.tokenSuffix))
            {
                return this.createToken(
                    source = source,
                    value = buffer.dropLast(this.tokenSuffix.length),
                    startIndex = startIndex,
                    untilIndex = index - 1,
                )
            }

            buffer.append(source.content[index++])
        }

        return null
    }

    protected abstract fun createToken(source: Source, value: CharSequence, startIndex: Int, untilIndex: Int): Token

    private fun consumeStringLiteral(startIndex: Int, buffer: StringBuilder, source: Source): Int
    {
        var index = startIndex

        if (buffer.endsWith("\""))
        {
            do
            {
                buffer.append(source.content[index++])

                if (buffer.endsWith("\\\""))
                {
                    continue
                }

                if (buffer.endsWith("\""))
                {
                    break
                }
            }
            while (index < source.content.length)
        }

        return index
    }

    private fun consumeRawStringLiteral(startIndex: Int, buffer: StringBuilder, source: Source): Int
    {
        var index = startIndex

        if (buffer.endsWith("\"\"\""))
        {
            do
            {
                buffer.append(source.content[index++])

                if (buffer.endsWith("\"\"\""))
                {
                    break
                }
            }
            while (index < source.content.length)
        }

        return index
    }
}
