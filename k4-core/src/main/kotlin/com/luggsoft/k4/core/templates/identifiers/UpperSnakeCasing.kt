package com.luggsoft.k4.core.templates.identifiers

object UpperSnakeCasing : Casing
{
    override fun apply(segments: Collection<String>): String = segments
        .joinToString(separator = "_") { segment -> segment.toUpperCase() }
}
