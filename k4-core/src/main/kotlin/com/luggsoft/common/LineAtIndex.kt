package com.luggsoft.common

fun CharSequence.lineNumberAtIndex(index: Int): Int = this
    .substring(0, index)
    .count { char -> char == '\n' }
    .plus(1)
