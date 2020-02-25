package com.luggsoft.k4.core.templates.identifiers

fun ofUpperCamelCase(name: String): String = Identifier(name)
    .toUpperCamelCase()
