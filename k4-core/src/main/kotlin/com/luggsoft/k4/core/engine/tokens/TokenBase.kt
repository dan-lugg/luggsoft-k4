package com.luggsoft.k4.core.engine.tokens

import com.luggsoft.k4.core.engine.Location

abstract class TokenBase(
    final override val location: Location
) : Token
