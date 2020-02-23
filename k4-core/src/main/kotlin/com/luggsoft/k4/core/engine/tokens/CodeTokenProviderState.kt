package com.luggsoft.k4.core.engine.tokens

import com.luggsoft.k4.core.SourceProvider
import com.luggsoft.k4.core.engine.DefaultLocation
import com.luggsoft.k4.core.io.EOF

open class CodeTokenProviderState : TokenProviderStateBase()
{
    override fun getNextToken(sourceProvider: SourceProvider, tokenProviderStateSetter: TokenProviderStateSetter): Token
    {
        val stringBuffer = StringBuffer()
        val startIndex = sourceProvider.sourceCharScanner.index
        while (true)
        {
            when (val readChar = sourceProvider.sourceCharScanner.read())
            {
                Char.EOF ->
                {
                    val location = DefaultLocation(
                        name = sourceProvider.name,
                        startIndex = startIndex,
                        untilIndex = sourceProvider.sourceCharScanner.index
                    )
                    throw TokenProviderStateException("Unexpected EOF at $location")
                }
                else ->
                {
                    stringBuffer.append(readChar)
                    if (stringBuffer.endsWith("#>"))
                    {
                        tokenProviderStateSetter(TextTokenProviderState.Default)
                        return CodeToken(
                            code = stringBuffer.removeSuffix("#>").toString(),
                            location = DefaultLocation(
                                name = sourceProvider.name,
                                startIndex = startIndex,
                                untilIndex = sourceProvider.sourceCharScanner.index
                            )
                        )
                    }
                }
            }
        }
    }

    internal object Default : CodeTokenProviderState()
}
