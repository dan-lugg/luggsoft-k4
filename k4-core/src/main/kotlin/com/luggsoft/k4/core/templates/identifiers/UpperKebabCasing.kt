package com.luggsoft.k4.core.templates.identifiers

object UpperKebabCasing : Casing
{
    override fun apply(segments: Collection<String>): String = segments
        .joinToString(separator = "-") { segment -> segment.toUpperCase() }
}
