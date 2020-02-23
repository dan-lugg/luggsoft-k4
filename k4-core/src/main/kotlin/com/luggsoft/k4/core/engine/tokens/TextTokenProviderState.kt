package com.luggsoft.k4.core.engine.tokens

import com.luggsoft.k4.core.SourceProvider
import com.luggsoft.k4.core.engine.DefaultLocation
import com.luggsoft.k4.core.io.EOF

open class TextTokenProviderState : TokenProviderStateBase()
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
                    return TextToken(
                        text = stringBuffer.toString(),
                        location = DefaultLocation(
                            name = sourceProvider.name,
                            startIndex = startIndex,
                            untilIndex = sourceProvider.sourceCharScanner.index
                        )
                    )
                }
                else ->
                {
                    stringBuffer.append(readChar)
                    if (stringBuffer.endsWith("<#"))
                    {
                        val tokenProviderState = when (sourceProvider.sourceCharScanner.read())
                        {
                            '!' -> CodeTokenProviderState.Default
                            '$' -> EchoTokenProviderState.Default
                            '@' -> InfoTokenProviderState.Default
                            else -> UnknownTokenProviderState
                        }
                        tokenProviderStateSetter(tokenProviderState)
                        return TextToken(
                            text = stringBuffer.removeSuffix("<#").toString(),
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

    internal object Default : TextTokenProviderState()
}

