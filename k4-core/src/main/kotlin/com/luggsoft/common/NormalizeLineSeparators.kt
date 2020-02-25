package com.luggsoft.common

fun CharSequence.normalizeLineSeparators(): CharSequence = this
    .toString()
    .replace("\r\n", "\n")
    .replace("\r", "\n")
