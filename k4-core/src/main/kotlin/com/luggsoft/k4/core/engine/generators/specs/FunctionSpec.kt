package com.luggsoft.k4.core.engine.generators.specs

import org.intellij.lang.annotations.Language
import kotlin.reflect.KClass

class FunctionSpec(
    val name: String,
    val returnType: KClass<*>,
    val parameterSpecs: List<ParameterSpec>,
    @Language("kotlin") val code: String
)
