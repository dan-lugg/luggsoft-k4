package com.luggsoft.k4.core.engine.generators.specs

import java.util.*

fun main()
{
    val fileSpec = FileSpec(
        name = "Test",
        packageName = "com.example.test",
        importTypes = listOf(
            UUID::class,
            Random::class
        ),
        functionSpecs = listOf(
            FunctionSpec(
                name = "doStuff",
                returnType = Long::class,
                parameterSpecs = listOf(
                    ParameterSpec(
                        name = "x",
                        type = Int::class
                    ),
                    ParameterSpec(
                        name = "y",
                        type = Int::class
                    ),
                    ParameterSpec(
                        name = "z",
                        type = Int::class
                    )
                ),
                code = "return (x + y + z).toLong()"
            )
        )
    )

    val specRenderer = SpecRenderer()
    specRenderer.renderFileSpec(fileSpec).also(::println)
}


///

