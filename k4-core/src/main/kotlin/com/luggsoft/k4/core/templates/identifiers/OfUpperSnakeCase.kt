package com.luggsoft.k4.core.templates.identifiers

fun ofUpperSnakeCase(name: String): String = Identifier(name)
    .toUpperSnakeCase()
