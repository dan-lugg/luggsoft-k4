package com.luggsoft.k4.core.templates.identifiers

object LowerKebabCasing : Casing
{
    override fun apply(segments: Collection<String>): String = segments
        .joinToString(separator = "-") { segment -> segment.toLowerCase() }
}
