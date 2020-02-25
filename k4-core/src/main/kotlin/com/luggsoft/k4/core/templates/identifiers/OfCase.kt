package com.luggsoft.k4.core.templates.identifiers

fun ofCase(name: String, casing: Casing): String = Identifier(name)
    .toCase(casing)
