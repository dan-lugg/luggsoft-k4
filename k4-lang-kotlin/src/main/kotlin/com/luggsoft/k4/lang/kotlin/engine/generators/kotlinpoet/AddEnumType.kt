package com.luggsoft.k4.lang.kotlin.engine.generators.kotlinpoet

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec.Builder
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.TypeSpec.Companion

fun Builder.addEnumType(name: String, block: TypeSpec.Builder.() -> Unit): Builder
{
    return TypeSpec.enumBuilder(name).also(block).build().let(this::addType)
}

fun Builder.addEnumType(className: ClassName, block: TypeSpec.Builder.() -> Unit): Builder
{
    return Companion.enumBuilder(className).also(block).build().let(this::addType)
}

fun TypeSpec.Builder.addEnumType(name: String, block: TypeSpec.Builder.() -> Unit): TypeSpec.Builder
{
    return Companion.enumBuilder(name).also(block).build().let(this::addType)
}

fun TypeSpec.Builder.addEnumType(className: ClassName, block: TypeSpec.Builder.() -> Unit): TypeSpec.Builder
{
    return Companion.enumBuilder(className).also(block).build().let(this::addType)
}
