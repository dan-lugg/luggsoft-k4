package com.luggsoft.k4.core.templates.identifiers

fun Casing.apply(name: String): String = Identifier(name)
    .toCase(this)
