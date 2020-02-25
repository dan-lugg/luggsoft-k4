package com.luggsoft.k4.lang.kotlin.engine.generators.kotlinpoet

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec.Builder
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.TypeSpec.Companion

fun Builder.addClassType(name: String, block: TypeSpec.Builder.() -> Unit): Builder
{
    return TypeSpec.classBuilder(name).also(block).build().let(this::addType)
}

fun Builder.addClassType(className: ClassName, block: TypeSpec.Builder.() -> Unit): Builder
{
    return Companion.classBuilder(className).also(block).build().let(this::addType)
}

fun TypeSpec.Builder.addClassType(name: String, block: TypeSpec.Builder.() -> Unit): TypeSpec.Builder
{
    return Companion.classBuilder(name).also(block).build().let(this::addType)
}

fun TypeSpec.Builder.addClassType(className: ClassName, block: TypeSpec.Builder.() -> Unit): TypeSpec.Builder
{
    return Companion.classBuilder(className).also(block).build().let(this::addType)
}
