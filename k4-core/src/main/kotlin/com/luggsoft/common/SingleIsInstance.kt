package com.luggsoft.common

inline fun <reified T> Iterable<*>.singleIsInstance(): T
{
    return this.single { element -> element is T } as T
}
