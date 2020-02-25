package com.luggsoft.k4.core.engine.tokenizers.tokens

import com.luggsoft.k4.core.engine.UnknownLocation

internal object UnknownToken : TokenBase(
    location = UnknownLocation
)
{
    override val content: String = String()
}
