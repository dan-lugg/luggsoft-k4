package com.luggsoft.k4.core.engine.tokenizers.tokens

import com.luggsoft.k4.core.engine.Location

object UnknownToken : Token
{
    override val location: Location = Location(
        name = String(),
        startIndex = -1,
        untilIndex = -1
    )

    override val content: String = String()
}
