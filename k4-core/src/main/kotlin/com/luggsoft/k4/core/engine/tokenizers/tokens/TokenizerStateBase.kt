package com.luggsoft.k4.core.engine.tokenizers.tokens

import com.luggsoft.k4.core.Source
import com.luggsoft.k4.core.engine.tokenizers.TokenizerSettings
import com.luggsoft.k4.core.internal.OBJECT_MAPPER
import com.luggsoft.common.columnNumberAtIndex
import com.luggsoft.common.lineNumberAtIndex

abstract class TokenizerStateBase(
    protected val tokenizerSettings: TokenizerSettings
) : TokenizerState
{
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

    protected fun getUnexpectedEOFTokenizerStateException(source: Source, startIndex: Int, untilIndex: Int): TokenizerStateException
    {
        val text = source.text.substring(startIndex, untilIndex)
        val lineNumber = source.text.lineNumberAtIndex(untilIndex)
        val columnNumber = source.text.columnNumberAtIndex(untilIndex)
        val message = "${source.name}:$lineNumber:$columnNumber, unexpected EOF in ${OBJECT_MAPPER.writeValueAsString(text)}"
        return TokenizerStateException(message)
    }

    protected fun tryDiscardTrailingLineSeparators(source: Source, startIndex: Int): Int
    {
        var index = startIndex
        if (this.tokenizerSettings.discardTrailingLineSeparators)
        {
            while (source.text[index] == '\n')
            {
                index++
            }
        }
        return index
    }
}
