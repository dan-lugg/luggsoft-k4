package com.luggsoft.k4.core

interface Settings<T : Enum<T>, U : Settings<T, U>>
{
    fun enable(flag: T): U

    fun disable(flag: T): U

    operator fun get(flag: T): Boolean
}
