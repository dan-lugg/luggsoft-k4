package com.luggsoft.k4.core.engine.tokens

import com.luggsoft.k4.core.engine.Location

class TextToken(
    val text: String,
    location: Location
) : TokenBase(
    location = location
)
