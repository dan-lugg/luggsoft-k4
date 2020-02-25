package com.luggsoft.k4.core.engine.tokenizers.tokens

import com.luggsoft.common.logger
import com.luggsoft.k4.core.Source
import com.luggsoft.k4.core.engine.tokenizers.TokenizerSettings

object UnknownTokenizerState : TokenizerStateBase(
    tokenizerSettings = TokenizerSettings.Default
)
{
    override fun getNextToken(source: Source, startIndex: Int, tokenizerStateSetter: TokenizerStateSetter): Token
    {
        this.logger.warn("Unknown token provider state")
        return UnknownToken
    }
}
