package com.luggsoft.k4.lang.kotlin.engine.generators.kotlinpoet

import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.AnnotationSpec.Companion
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec.Builder
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import kotlin.reflect.KClass

fun Builder.addAnnotation(type: ClassName, block: AnnotationSpec.Builder.() -> Unit): Builder
{
    return AnnotationSpec.builder(type).also(block).build().let(this::addAnnotation)
}

fun Builder.addAnnotation(type: Class<out Annotation>, block: AnnotationSpec.Builder.() -> Unit): Builder
{
    return Companion.builder(type).also(block).build().let(this::addAnnotation)
}

fun Builder.addAnnotation(type: KClass<out Annotation>, block: AnnotationSpec.Builder.() -> Unit): Builder
{
    return Companion.builder(type).also(block).build().let(this::addAnnotation)
}

fun PropertySpec.Builder.addAnnotation(type: ClassName, block: AnnotationSpec.Builder.() -> Unit): PropertySpec.Builder
{
    return Companion.builder(type).also(block).build().let(this::addAnnotation)
}

fun PropertySpec.Builder.addAnnotation(type: Class<out Annotation>, block: AnnotationSpec.Builder.() -> Unit): PropertySpec.Builder
{
    return Companion.builder(type).also(block).build().let(this::addAnnotation)
}

fun PropertySpec.Builder.addAnnotation(type: KClass<out Annotation>, block: AnnotationSpec.Builder.() -> Unit): PropertySpec.Builder
{
    return Companion.builder(type).also(block).build().let(this::addAnnotation)
}

fun TypeSpec.Builder.addAnnotation(type: ClassName, block: AnnotationSpec.Builder.() -> Unit): TypeSpec.Builder
{
    return Companion.builder(type).also(block).build().let(this::addAnnotation)
}

fun TypeSpec.Builder.addAnnotation(type: Class<out Annotation>, block: AnnotationSpec.Builder.() -> Unit): TypeSpec.Builder
{
    return Companion.builder(type).also(block).build().let(this::addAnnotation)
}

fun TypeSpec.Builder.addAnnotation(type: KClass<out Annotation>, block: AnnotationSpec.Builder.() -> Unit): TypeSpec.Builder
{
    return Companion.builder(type).also(block).build().let(this::addAnnotation)
}

fun FunSpec.Builder.addAnnotation(type: ClassName, block: AnnotationSpec.Builder.() -> Unit): FunSpec.Builder
{
    return Companion.builder(type).also(block).build().let(this::addAnnotation)
}

fun FunSpec.Builder.addAnnotation(type: Class<out Annotation>, block: AnnotationSpec.Builder.() -> Unit): FunSpec.Builder
{
    return Companion.builder(type).also(block).build().let(this::addAnnotation)
}

fun FunSpec.Builder.addAnnotation(type: KClass<out Annotation>, block: AnnotationSpec.Builder.() -> Unit): FunSpec.Builder
{
    return Companion.builder(type).also(block).build().let(this::addAnnotation)
}
