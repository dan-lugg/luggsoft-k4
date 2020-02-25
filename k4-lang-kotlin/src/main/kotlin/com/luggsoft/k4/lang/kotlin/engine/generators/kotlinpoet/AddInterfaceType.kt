package com.luggsoft.k4.lang.kotlin.engine.generators.kotlinpoet

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec.Builder
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.TypeSpec.Companion

fun Builder.addInterfaceType(name: String, block: TypeSpec.Builder.() -> Unit): Builder
{
    return TypeSpec.interfaceBuilder(name).also(block).build().let(this::addType)
}

fun Builder.addInterfaceType(className: ClassName, block: TypeSpec.Builder.() -> Unit): Builder
{
    return Companion.interfaceBuilder(className).also(block).build().let(this::addType)
}

fun TypeSpec.Builder.addInterfaceType(name: String, block: TypeSpec.Builder.() -> Unit): TypeSpec.Builder
{
    return Companion.interfaceBuilder(name).also(block).build().let(this::addType)
}

fun TypeSpec.Builder.addInterfaceType(className: ClassName, block: TypeSpec.Builder.() -> Unit): TypeSpec.Builder
{
    return Companion.interfaceBuilder(className).also(block).build().let(this::addType)
}
