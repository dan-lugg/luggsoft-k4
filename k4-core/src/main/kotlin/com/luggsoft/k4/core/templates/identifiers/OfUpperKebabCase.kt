package com.luggsoft.k4.core.templates.identifiers

fun ofUpperKebabCase(name: String): String = Identifier(name)
    .toUpperKebabCase()
