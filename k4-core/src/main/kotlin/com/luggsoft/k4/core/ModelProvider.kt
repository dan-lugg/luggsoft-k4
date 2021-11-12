package com.luggsoft.k4.core

import kotlin.reflect.KClass

interface ModelProvider
{
    fun <T : Any> getModel(name: String, kClass: KClass<T>): T
}
