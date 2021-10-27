package com.luggsoft.k4.core.vx.io

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonTypeInfo

@JsonTypeInfo(use = JsonTypeInfo.Id.MINIMAL_CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@type")
interface Source : CharSequence
{
    val name: String

    @get:JsonIgnore
    val content: CharSequence
}

