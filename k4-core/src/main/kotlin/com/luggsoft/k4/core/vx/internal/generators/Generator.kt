package com.luggsoft.k4.core.vx.internal.generators

import com.luggsoft.k4.core.vx.internal.tokenizers.Token
import java.io.Writer

interface Generator
{
    fun generate(tokens: Iterable<Token>, writer: Writer)
}
