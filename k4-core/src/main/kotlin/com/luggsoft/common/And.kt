package com.luggsoft.common

inline fun Boolean.and(crossinline block: () -> Boolean): Boolean = this and block()
