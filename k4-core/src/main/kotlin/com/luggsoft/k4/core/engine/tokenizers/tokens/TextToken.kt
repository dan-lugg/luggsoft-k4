package com.luggsoft.k4.core.engine.tokenizers.tokens

import com.luggsoft.k4.core.engine.Location

data class TextToken(
    val text: String,
    override val location: Location
) : Token
{
    override val content: String
        get() = this.text
}
