package com.luggsoft.k4.core.engine.tokens

import com.luggsoft.k4.core.engine.Location

class CodeToken(
    val code: String,
    location: Location
) : TokenBase(
    location = location
)
