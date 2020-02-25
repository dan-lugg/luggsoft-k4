package com.luggsoft.k4.core.templates.identifiers

fun String.toCase(casing: Casing): String = ofCase(this, casing)
