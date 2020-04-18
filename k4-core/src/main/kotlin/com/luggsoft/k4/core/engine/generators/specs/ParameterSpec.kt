package com.luggsoft.k4.core.engine.generators.specs

import kotlin.reflect.KClass

class ParameterSpec(
    val name: String,
    val type: KClass<*>
)
