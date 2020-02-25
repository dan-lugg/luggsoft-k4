package com.luggsoft.k4.core.engine.tokenizers

import com.luggsoft.k4.core.Source
import com.luggsoft.k4.core.engine.tokenizers.tokens.Token

interface Tokenizer
{
    @Throws(TokenizerException::class)
    fun tokenizeSource(source: Source, startIndex: Int = 0): List<Token>
}
