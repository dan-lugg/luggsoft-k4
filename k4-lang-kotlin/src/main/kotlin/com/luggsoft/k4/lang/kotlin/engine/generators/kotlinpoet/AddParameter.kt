package com.luggsoft.k4.lang.kotlin.engine.generators.kotlinpoet

import com.squareup.kotlinpoet.FunSpec.Builder
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.ParameterSpec.Companion
import com.squareup.kotlinpoet.TypeName
import java.lang.reflect.Type
import kotlin.reflect.KClass

fun Builder.addParameter(name: String, type: TypeName, vararg modifiers: KModifier, block: ParameterSpec.Builder.() -> Unit): Builder
{
    return ParameterSpec.builder(name, type, *modifiers).also(block).build().let(this::addParameter)
}

fun Builder.addParameter(name: String, type: TypeName, modifiers: Iterable<KModifier>, block: ParameterSpec.Builder.() -> Unit): Builder
{
    return Companion.builder(name, type, modifiers).also(block).build().let(this::addParameter)
}

fun Builder.addParameter(name: String, type: Type, vararg modifiers: KModifier, block: ParameterSpec.Builder.() -> Unit): Builder
{
    return Companion.builder(name, type, *modifiers).also(block).build().let(this::addParameter)
}

fun Builder.addParameter(name: String, type: Type, modifiers: Iterable<KModifier>, block: ParameterSpec.Builder.() -> Unit): Builder
{
    return Companion.builder(name, type, modifiers).also(block).build().let(this::addParameter)
}

fun Builder.addParameter(name: String, type: KClass<*>, vararg modifiers: KModifier, block: ParameterSpec.Builder.() -> Unit): Builder
{
    return Companion.builder(name, type, *modifiers).also(block).build().let(this::addParameter)
}

fun Builder.addParameter(name: String, type: KClass<*>, modifiers: Iterable<KModifier>, block: ParameterSpec.Builder.() -> Unit): Builder
{
    return Companion.builder(name, type, modifiers).also(block).build().let(this::addParameter)
}
