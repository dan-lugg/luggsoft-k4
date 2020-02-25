package com.luggsoft.k4.core.engine.generators

import com.luggsoft.k4.core.engine.tokenizers.tokens.Token

interface Generator
{
    @Throws(GeneratorException::class)
    fun generateScript(tokens: List<Token>): String
}
