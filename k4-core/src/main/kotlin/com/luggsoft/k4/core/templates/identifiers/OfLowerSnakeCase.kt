package com.luggsoft.k4.core.templates.identifiers

fun ofLowerSnakeCase(name: String): String = Identifier(name)
    .toLowerSnakeCase()
