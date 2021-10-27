package com.luggsoft.k4.core.vx.internal.tokenizers

import com.luggsoft.k4.core.vx.io.Source

interface TokenReader
{
    fun readToken(source: Source, startIndex: Int): Token?
}
