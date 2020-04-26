package com.luggsoft.k4.scaffolding.internals

import com.fasterxml.jackson.core.util.DefaultIndenter
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter
import com.fasterxml.jackson.databind.ObjectWriter
import com.luggsoft.k4.scaffolding.internals.OBJECT_MAPPER

internal val OBJECT_WRITER: ObjectWriter by lazy {
    val indenter = DefaultIndenter()

    val prettyPrinter = DefaultPrettyPrinter()
        .withObjectIndenter(indenter)
        .withArrayIndenter(indenter)

    return@lazy OBJECT_MAPPER.writer()
        .with(prettyPrinter)
}
