package com.luggsoft.k4.core.engine.generators.formatters

class UnindentFormatter : Formatter
{
    override fun format(input: String): String = input
        .lines()
        .joinToString(separator = "\n", transform = String::trim)
}
