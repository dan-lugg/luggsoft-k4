package com.luggsoft.k4.v2.templates

fun CharSequence.kotlinEscape(): String = this
    .kotlinEscape(StringBuilder())
    .toString()

fun CharSequence.kotlinEscape(buffer: Appendable): Appendable
{
    this.map(Char::kotlinEscape).onEach(buffer::append)
    return buffer
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
