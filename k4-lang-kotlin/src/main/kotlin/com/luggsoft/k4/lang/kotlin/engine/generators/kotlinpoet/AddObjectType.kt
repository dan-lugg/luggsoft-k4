package com.luggsoft.k4.lang.kotlin.engine.generators.kotlinpoet

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec.Builder
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.TypeSpec.Companion

fun Builder.addObjectType(name: String, block: TypeSpec.Builder.() -> Unit): Builder
{
    return TypeSpec.objectBuilder(name).also(block).build().let(this::addType)
}

fun Builder.addObjectType(className: ClassName, block: TypeSpec.Builder.() -> Unit): Builder
{
    return Companion.objectBuilder(className).also(block).build().let(this::addType)
}

fun TypeSpec.Builder.addObjectType(name: String, block: TypeSpec.Builder.() -> Unit): TypeSpec.Builder
{
    return Companion.objectBuilder(name).also(block).build().let(this::addType)
}

fun TypeSpec.Builder.addObjectType(className: ClassName, block: TypeSpec.Builder.() -> Unit): TypeSpec.Builder
{
    return Companion.objectBuilder(className).also(block).build().let(this::addType)
}
