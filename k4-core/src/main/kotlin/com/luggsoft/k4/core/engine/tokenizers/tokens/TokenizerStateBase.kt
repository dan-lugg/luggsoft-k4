package com.luggsoft.k4.core.engine.tokenizers.tokens

import com.luggsoft.common.text.columnIndexAt
import com.luggsoft.common.text.lineIndexAt
import com.luggsoft.k4.core.Source
import com.luggsoft.k4.core.engine.tokenizers.TokenizerSettings
import com.luggsoft.k4.core.internal.OBJECT_MAPPER

abstract class TokenizerStateBase(
    protected val tokenizerSettings: TokenizerSettings
) : TokenizerState
{
    /**
     * TODO
     *
     * @param source
     * @param stringBuffer
     * @param startIndex
     * @return
     */
    protected fun tryConsumeString(source: Source, stringBuffer: StringBuffer, startIndex: Int): Int
    {
        var index = startIndex
        if (stringBuffer.endsWith("\""))
        {
            while (true)
            {
                stringBuffer.append(source.text[index++])
                if (stringBuffer.endsWith("\""))
                {
                    break
                }
            }
        }
        return index
    }

    /**
     * TODO
     *
     * @param source
     * @param stringBuffer
     * @param startIndex
     * @return
     */
    protected fun tryConsumeRawString(source: Source, stringBuffer: StringBuffer, startIndex: Int): Int
    {
        var index = startIndex
        if (stringBuffer.endsWith("\"\"\""))
        {
            while (true)
            {
                stringBuffer.append(source.text[index++])
                if (stringBuffer.endsWith("\"\"\""))
                {
                    break
                }
            }
        }
        return index
    }

    /**
     * TODO
     *
     * @param source
     * @param startIndex
     * @return
     */
    protected fun tryDiscardTrailingLineSeparators(source: Source, startIndex: Int): Int
    {
        var index = startIndex
        if (this.tokenizerSettings.discardTrailingLineSeparators)
        {
            while (index < source.text.length && source.text[index] == '\n')
            {
                index++
            }
        }
        return index
    }

    /**
     * TODO
     *
     * @param source
     * @param startIndex
     * @param untilIndex
     * @return
     */
    protected fun getUnexpectedEOFTokenizerStateException(source: Source, startIndex: Int, untilIndex: Int): TokenizerStateException
    {
        val text = source.text.substring(startIndex, untilIndex)
        val lineNumber = source.text.lineIndexAt(untilIndex)
        val columnNumber = source.text.columnIndexAt(untilIndex)
        val message = "${source.name}:$lineNumber:$columnNumber, unexpected EOF in ${OBJECT_MAPPER.writeValueAsString(text)}"
        return TokenizerStateException(message)
    }
}
