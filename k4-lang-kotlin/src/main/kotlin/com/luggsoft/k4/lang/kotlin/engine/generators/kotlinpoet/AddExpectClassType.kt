package com.luggsoft.k4.lang.kotlin.engine.generators.kotlinpoet

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec.Builder
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.TypeSpec.Companion

fun Builder.addExpectClassType(name: String, block: TypeSpec.Builder.() -> Unit): Builder
{
    return TypeSpec.expectClassBuilder(name).also(block).build().let(this::addType)
}

fun Builder.addExpectClassType(className: ClassName, block: TypeSpec.Builder.() -> Unit): Builder
{
    return Companion.expectClassBuilder(className).also(block).build().let(this::addType)
}

fun TypeSpec.Builder.addExpectClassType(name: String, block: TypeSpec.Builder.() -> Unit): TypeSpec.Builder
{
    return Companion.expectClassBuilder(name).also(block).build().let(this::addType)
}

fun TypeSpec.Builder.addExpectClassType(className: ClassName, block: TypeSpec.Builder.() -> Unit): TypeSpec.Builder
{
    return Companion.expectClassBuilder(className).also(block).build().let(this::addType)
}
