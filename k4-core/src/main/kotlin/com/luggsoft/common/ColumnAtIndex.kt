package com.luggsoft.common

fun CharSequence.columnNumberAtIndex(index: Int): Int = this
    .substring(0, index)
    .substringAfterLast('\n')
    .length
    .plus(1)
