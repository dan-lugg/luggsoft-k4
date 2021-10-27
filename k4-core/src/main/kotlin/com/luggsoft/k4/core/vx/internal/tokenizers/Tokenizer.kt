package com.luggsoft.k4.core.vx.internal.tokenizers

import com.luggsoft.k4.core.vx.io.Source

interface Tokenizer
{
    fun tokenize(source: Source, startIndex: Int = 0): List<Token>
}
