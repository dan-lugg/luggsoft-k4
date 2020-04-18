package com.luggsoft.common

fun CharSequence.columnNumberAtIndex(index: Int): Int
{
    val zeroBasedColumn = this
        .substring(0, index)
        .substringAfterLast('\n')
        .length
    return zeroBasedColumn + 1
}
