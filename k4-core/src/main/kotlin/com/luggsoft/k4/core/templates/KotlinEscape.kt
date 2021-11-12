package com.luggsoft.k4.core.templates

fun String.kotlinEscape(): String
{
    val escapedStringBuilder = StringBuilder()

    for (char in this)
    {
        val escapedChar = char.kotlinEscape()
        escapedStringBuilder.append(escapedChar)
    }

    return escapedStringBuilder.toString()
}

fun Char.kotlinEscape(): String = when
{
    this == '\b' -> "\\b"
    this == '\t' -> "\\t"
    this == '\n' -> "\\n"
    this == '\r' -> "\\r"
    this == '\"' -> "\\\""
    this == '\$' -> "\\\$"
    this == '\\' -> "\\\\"
    this.isISOControl() -> String.format("\\u%04x", this.toInt())
    else -> this.toString()
}
