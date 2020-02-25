package com.luggsoft.k4.core.engine.tokenizers.tokens

import com.luggsoft.k4.core.engine.Location

class NoteToken(
    val note: String,
    location: Location
) : TokenBase(
    location = location
)
{
    override val content: String
        get() = this.note
}
