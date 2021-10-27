package com.luggsoft.k4.core.vx.util

import kotlin.reflect.KClass

fun kClassForName(className: String): KClass<*> = when (className)
{
    "kotlin.Any" -> Any::class
    else -> Class.forName(className).kotlin
}
