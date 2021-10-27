package com.luggsoft.k4.core.templates.identifiers

import com.luggsoft.common.EMPTY_STRING
import com.luggsoft.common.collections.head
import com.luggsoft.common.collections.tail

object LowerCamelCasing : Casing
{
    @Suppress("NAME_SHADOWING")
    override fun apply(segments: Collection<String>): String = segments
        .map { segment -> segment.toLowerCase().capitalize() }
        .let { segments -> segments.head.toLowerCase() + segments.tail.joinToString(separator = EMPTY_STRING) }
}
