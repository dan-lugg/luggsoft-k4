package com.luggsoft.k4.lang.kotlin.engine.generators.kotlinpoet

import com.squareup.kotlinpoet.FileSpec.Builder
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.PropertySpec.Companion
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.TypeSpec
import java.lang.reflect.Type
import kotlin.reflect.KClass

fun Builder.addProperty(name: String, type: TypeName, vararg modifiers: KModifier, block: PropertySpec.Builder.() -> Unit): Builder
{
    return PropertySpec.builder(name, type, *modifiers).also(block).build().let(this::addProperty)
}

fun Builder.addProperty(name: String, type: TypeName, modifiers: Iterable<KModifier>, block: PropertySpec.Builder.() -> Unit): Builder
{
    return Companion.builder(name, type, modifiers).also(block).build().let(this::addProperty)
}

fun Builder.addProperty(name: String, type: Type, vararg modifiers: KModifier, block: PropertySpec.Builder.() -> Unit): Builder
{
    return Companion.builder(name, type, *modifiers).also(block).build().let(this::addProperty)
}

fun Builder.addProperty(name: String, type: Type, modifiers: Iterable<KModifier>, block: PropertySpec.Builder.() -> Unit): Builder
{
    return Companion.builder(name, type, modifiers).also(block).build().let(this::addProperty)
}

fun Builder.addProperty(name: String, type: KClass<*>, vararg modifiers: KModifier, block: PropertySpec.Builder.() -> Unit): Builder
{
    return Companion.builder(name, type, *modifiers).also(block).build().let(this::addProperty)
}

fun Builder.addProperty(name: String, type: KClass<*>, modifiers: Iterable<KModifier>, block: PropertySpec.Builder.() -> Unit): Builder
{
    return Companion.builder(name, type, modifiers).also(block).build().let(this::addProperty)
}

fun TypeSpec.Builder.addProperty(name: String, type: TypeName, vararg modifiers: KModifier, block: PropertySpec.Builder.() -> Unit): TypeSpec.Builder
{
    return Companion.builder(name, type, *modifiers).also(block).build().let(this::addProperty)
}

fun TypeSpec.Builder.addProperty(name: String, type: TypeName, modifiers: Iterable<KModifier>, block: PropertySpec.Builder.() -> Unit): TypeSpec.Builder
{
    return Companion.builder(name, type, modifiers).also(block).build().let(this::addProperty)
}

fun TypeSpec.Builder.addProperty(name: String, type: Type, vararg modifiers: KModifier, block: PropertySpec.Builder.() -> Unit): TypeSpec.Builder
{
    return Companion.builder(name, type, *modifiers).also(block).build().let(this::addProperty)
}

fun TypeSpec.Builder.addProperty(name: String, type: Type, modifiers: Iterable<KModifier>, block: PropertySpec.Builder.() -> Unit): TypeSpec.Builder
{
    return Companion.builder(name, type, modifiers).also(block).build().let(this::addProperty)
}

fun TypeSpec.Builder.addProperty(name: String, type: KClass<*>, vararg modifiers: KModifier, block: PropertySpec.Builder.() -> Unit): TypeSpec.Builder
{
    return Companion.builder(name, type, *modifiers).also(block).build().let(this::addProperty)
}

fun TypeSpec.Builder.addProperty(name: String, type: KClass<*>, modifiers: Iterable<KModifier>, block: PropertySpec.Builder.() -> Unit): TypeSpec.Builder
{
    return Companion.builder(name, type, modifiers).also(block).build().let(this::addProperty)
}
