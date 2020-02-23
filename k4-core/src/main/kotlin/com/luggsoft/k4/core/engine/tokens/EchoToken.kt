package com.luggsoft.k4.core.engine.tokens

import com.luggsoft.k4.core.engine.Location

class EchoToken(
    val echo: String,
    location: Location
) : TokenBase(
    location = location
)
