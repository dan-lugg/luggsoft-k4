package com.luggsoft.k4.core.templates.identifiers

import com.luggsoft.common.EMPTY_STRING

object UpperCamelCasing : Casing
{
    override fun apply(segments: Collection<String>): String = segments
        .joinToString(separator = EMPTY_STRING) { segment -> segment.toLowerCase().capitalize() }
}
