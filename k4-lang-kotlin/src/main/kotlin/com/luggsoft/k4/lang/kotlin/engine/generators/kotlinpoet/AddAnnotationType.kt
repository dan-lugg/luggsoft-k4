package com.luggsoft.k4.lang.kotlin.engine.generators.kotlinpoet

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec.Builder
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.TypeSpec.Companion

fun Builder.addAnnotationType(name: String, block: TypeSpec.Builder.() -> Unit): Builder
{
    return TypeSpec.annotationBuilder(name).also(block).build().let(this::addType)
}

fun Builder.addAnnotationType(className: ClassName, block: TypeSpec.Builder.() -> Unit): Builder
{
    return Companion.annotationBuilder(className).also(block).build().let(this::addType)
}

fun TypeSpec.Builder.addAnnotationType(name: String, block: TypeSpec.Builder.() -> Unit): TypeSpec.Builder
{
    return Companion.annotationBuilder(name).also(block).build().let(this::addType)
}

fun TypeSpec.Builder.addAnnotationType(className: ClassName, block: TypeSpec.Builder.() -> Unit): TypeSpec.Builder
{
    return Companion.annotationBuilder(className).also(block).build().let(this::addType)
}
