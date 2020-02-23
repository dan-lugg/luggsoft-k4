package com.luggsoft.k4.core.engine.tokens

import com.luggsoft.k4.core.SourceProvider
import com.luggsoft.k4.core.engine.tokens.Token

interface TokenProvider
{
    fun getTokens(sourceProvider: SourceProvider): List<Token>
}
