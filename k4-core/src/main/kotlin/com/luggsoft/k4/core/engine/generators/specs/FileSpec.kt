package com.luggsoft.k4.core.engine.generators.specs

import kotlin.reflect.KClass

class FileSpec(
    val name: String,
    val packageName: String,
    val importTypes: List<KClass<*>>,
    val functionSpecs: List<FunctionSpec>
)
