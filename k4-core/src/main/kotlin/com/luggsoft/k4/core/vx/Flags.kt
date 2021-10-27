package com.luggsoft.k4.core.vx

interface Flags<T : Enum<T>, U : Flags<T, U>>
{
    fun enable(flag: T): U
    fun disable(flag: T): U
    fun getValue(flag: T): Boolean
}
