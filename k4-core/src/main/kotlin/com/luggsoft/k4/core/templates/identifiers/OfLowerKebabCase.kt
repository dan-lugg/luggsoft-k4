package com.luggsoft.k4.core.templates.identifiers

fun ofLowerKebabCase(name: String): String = Identifier(name)
    .toLowerKebabCase()
