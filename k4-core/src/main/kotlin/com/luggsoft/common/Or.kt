package com.luggsoft.common

inline fun Boolean.or(crossinline block: () -> Boolean): Boolean = this or block()
