package com.luggsoft.k4.core.vx.internal.tokenizers

import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.luggsoft.k4.core.vx.Location

@JsonTypeInfo(use = JsonTypeInfo.Id.MINIMAL_CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@type")
interface Token
{
    val value: CharSequence
    val location: Location
}
