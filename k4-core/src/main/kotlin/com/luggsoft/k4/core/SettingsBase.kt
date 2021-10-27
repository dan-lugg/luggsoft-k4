package com.luggsoft.k4.core

abstract class SettingsBase<T : Enum<T>, U : Settings<T, U>>(
    protected val flagMap: Map<T, Boolean>,
) : Settings<T, U>
{
    final override fun get(flag: T): Boolean = this.flagMap[flag] ?: false

    protected fun Map<T, Boolean>.withEnabled(flag: T): Map<T, Boolean> = this + (flag to true)

    protected fun Map<T, Boolean>.withDisabled(flag: T): Map<T, Boolean> = this + (flag to false)
}
