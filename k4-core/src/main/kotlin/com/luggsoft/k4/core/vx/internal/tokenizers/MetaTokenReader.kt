package com.luggsoft.k4.core.vx.internal.tokenizers

import com.luggsoft.k4.core.vx.Location
import com.luggsoft.k4.core.vx.io.Source

class MetaTokenReader : TokenReaderBase()
{
    override val tokenPrefix: String = "<#@"

    override val tokenSuffix: String = "#>"

    override fun createToken(source: Source, value: CharSequence, startIndex: Int, untilIndex: Int): Token = MetaToken(
        value = value,
        location = Location(
            source = source,
            startIndex = startIndex,
            untilIndex = untilIndex
        )
    )
}
