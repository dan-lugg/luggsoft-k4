package com.luggsoft.k4.core.templates.identifiers

fun ofLowerCamelCase(name: String): String = Identifier(name)
    .toLowerCamelCase()
