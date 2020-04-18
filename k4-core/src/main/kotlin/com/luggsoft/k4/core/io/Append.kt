package com.luggsoft.k4.core.io

fun Appendable.append(value: Any): Appendable
{
    return value.toString().let(this::append)
}
