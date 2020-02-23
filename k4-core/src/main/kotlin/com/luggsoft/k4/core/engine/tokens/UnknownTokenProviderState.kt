package com.luggsoft.k4.core.engine.tokens

import com.luggsoft.k4.core.SourceProvider
import com.luggsoft.k4.core.util.logger

internal object UnknownTokenProviderState : TokenProviderStateBase()
{
    override fun getNextToken(sourceProvider: SourceProvider, tokenProviderStateSetter: TokenProviderStateSetter): Token
    {
        this.logger.warn("Unknown token provider state")
        return UnknownToken
    }
}
