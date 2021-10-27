package com.luggsoft.k4.core.vx.internal.tokenizers

import com.luggsoft.k4.core.vx.Location
import com.luggsoft.k4.core.vx.io.Source

class PrintTokenReader : TokenReaderBase()
{
    override val tokenPrefix: String = "<#="

    override val tokenSuffix: String = "#>"

    override fun createToken(source: Source, value: CharSequence, startIndex: Int, untilIndex: Int): Token = PrintToken(
        value = value,
        location = Location(
            source = source,
            startIndex = startIndex,
            untilIndex = untilIndex
        )
    )
}
