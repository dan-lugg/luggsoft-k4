package com.luggsoft.k4.core.engine.generators

import kotlin.reflect.KClass

data class DefaultScript(
    override val name: String,
    override val code: String,
    override val modelClass: KClass<*>
) : Script
