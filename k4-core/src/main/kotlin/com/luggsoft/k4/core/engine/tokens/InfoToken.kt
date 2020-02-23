package com.luggsoft.k4.core.engine.tokens

import com.luggsoft.k4.core.engine.Location

class InfoToken(
    val info: String,
    location: Location
) : TokenBase(
    location = location
)
