package com.luggsoft.k4.core.engine.tokens

import com.luggsoft.k4.core.SourceProvider

interface TokenProviderState
{
    @Throws(TokenProviderStateException::class)
    fun getNextToken(sourceProvider: SourceProvider, tokenProviderStateSetter: TokenProviderStateSetter): Token
}
