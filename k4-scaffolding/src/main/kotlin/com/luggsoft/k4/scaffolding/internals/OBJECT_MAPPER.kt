package com.luggsoft.k4.scaffolding.internals

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature.INDENT_OUTPUT
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

internal val OBJECT_MAPPER: ObjectMapper by lazy {
    return@lazy jacksonObjectMapper()
        .findAndRegisterModules()
        .enable(INDENT_OUTPUT)
}
