package com.luggsoft.k4.core.engine.tokens

import com.luggsoft.k4.core.SourceProvider
import com.luggsoft.k4.core.io.EOF
import com.luggsoft.k4.core.io.SeekMode
import com.luggsoft.k4.core.util.logger

abstract class TokenProviderBase : TokenProvider
{
    private var tokenProviderState: TokenProviderState = TextTokenProviderState()

    final override fun getTokens(sourceProvider: SourceProvider): List<Token>
    {
        return this.getTokenSequence(sourceProvider).toList()
    }

    private fun getTokenSequence(sourceProvider: SourceProvider): Sequence<Token>
    {
        return sequence {
            val name = sourceProvider.name
            sourceProvider.sourceCharScanner.seek(0, SeekMode.ABSOLUTE)
            peeking@ while (true)
            {
                val startIndex = sourceProvider.sourceCharScanner.index
                when (val peekChar = sourceProvider.sourceCharScanner.peek())
                {
                    Char.EOF ->
                    {
                        this.logger.debug("$peekChar@[$name:$startIndex]")
                        this@sequence.yield(UnknownToken)
                        break@peeking
                    }
                    else ->
                    {
                        val readChar = sourceProvider.sourceCharScanner.read()
                        val untilIndex = sourceProvider.sourceCharScanner.index
                        this.logger.debug("$readChar@[$name:$startIndex:$untilIndex]")
                        this@sequence.yield(UnknownToken)
                    }
                }
            }
        }
    }

    private fun setTokenProviderState(tokenProviderState: TokenProviderState)
    {

    }
}
