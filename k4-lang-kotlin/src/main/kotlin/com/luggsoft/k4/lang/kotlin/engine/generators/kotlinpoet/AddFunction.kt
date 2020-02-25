package com.luggsoft.k4.lang.kotlin.engine.generators.kotlinpoet

import com.squareup.kotlinpoet.FileSpec.Builder
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.FunSpec.Companion
import com.squareup.kotlinpoet.TypeSpec

fun Builder.addFunction(name: String, block: FunSpec.Builder.() -> Unit): Builder
{
    return FunSpec.builder(name).also(block).build().let(this::addFunction)
}

fun TypeSpec.Builder.addFunction(name: String, block: FunSpec.Builder.() -> Unit): TypeSpec.Builder
{
    return Companion.builder(name).also(block).build().let(this::addFunction)
}
