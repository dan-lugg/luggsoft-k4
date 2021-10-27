package com.luggsoft.k4.core.vx

abstract class FlagsBase<T : Enum<T>, U : Flags<T, U>> : Flags<T, U>
{
    private val flagMap: MutableMap<T, Boolean> = mutableMapOf()

    @Suppress("UNCHECKED_CAST")
    final override fun enable(flag: T): U
    {
        this.flagMap[flag] = true
        return this as U
    }

    @Suppress("UNCHECKED_CAST")
    final override fun disable(flag: T): U
    {
        this.flagMap[flag] = false
        return this as U
    }

    final override fun getValue(flag: T): Boolean = this.flagMap[flag] ?: false
}
