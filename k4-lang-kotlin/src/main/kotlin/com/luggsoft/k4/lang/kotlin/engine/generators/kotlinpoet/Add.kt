package com.luggsoft.k4.lang.kotlin.engine.generators.kotlinpoet

import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.CodeBlock.Builder

fun Builder.add(block: Builder.() -> Unit): Builder
{
    return CodeBlock.builder().also(block).build().let(this::add)
}
