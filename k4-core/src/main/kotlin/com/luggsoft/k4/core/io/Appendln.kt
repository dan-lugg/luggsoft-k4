package com.luggsoft.k4.core.io

fun Appendable.appendln(value: Any): Appendable = value.toString().let(this::appendln)
