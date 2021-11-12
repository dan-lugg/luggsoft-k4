package com.luggsoft.k4.core

import kotlin.reflect.KClass

class DefaultModelProvider(
    private val modelNameMap: Map<String, Any>,
) : ModelProvider
{
    @Suppress("UNCHECKED_CAST")
    override fun <T : Any> getModel(name: String, kClass: KClass<T>): T = this.modelNameMap[name] as T
}
