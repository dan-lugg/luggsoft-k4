package com.luggsoft.k4.core.vx.internal.tokenizers

import com.luggsoft.k4.core.vx.Location

data class PrintToken(
    override val value: CharSequence,
    override val location: Location,
) : Token
