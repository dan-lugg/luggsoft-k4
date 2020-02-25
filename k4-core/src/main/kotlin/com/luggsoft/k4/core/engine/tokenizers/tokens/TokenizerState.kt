package com.luggsoft.k4.core.engine.tokenizers.tokens

import com.luggsoft.k4.core.Source

interface TokenizerState
{
    fun getNextToken(source: Source, startIndex: Int, tokenizerStateSetter: TokenizerStateSetter): Token
}
