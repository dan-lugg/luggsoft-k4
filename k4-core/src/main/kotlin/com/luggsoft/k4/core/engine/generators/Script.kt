package com.luggsoft.k4.core.engine.generators

import kotlin.reflect.KClass

interface Script
{
    val name: String
    val code: String
    val modelClass: KClass<*>
}
