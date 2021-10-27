package com.luggsoft.k4.core.vx.internal.formatters

class DefaultFormatter : Formatter
{
    override fun format(input: String): String = input.lines()
        .map(String::trimStart)
        .filter(String::isNotEmpty)
        .joinToString(
            separator = System.lineSeparator(),
        )

    object Instance : Formatter by DefaultFormatter()
}
