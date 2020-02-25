package com.luggsoft.k4.lang.kotlin.engine.generators.kotlinpoet

import com.luggsoft.k4.lang.kotlin.engine.generators.EMPTY_STRING
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.PropertySpec.Builder

fun Builder.setter(block: FunSpec.Builder.() -> Unit): Builder
{
    return FunSpec.builder(EMPTY_STRING).also(block).build().let(this::setter)
}
