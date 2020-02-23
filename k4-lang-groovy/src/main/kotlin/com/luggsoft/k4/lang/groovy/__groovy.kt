package com.luggsoft.k4.lang.groovy

import com.luggsoft.k4.core.SourceProviderBase

fun main()
{
    val sourceProvider = object : SourceProviderBase(
        name = "test",
        source = "Hello world! How are you doing today?"
    )
    {
        // TODO: Nothing for now
    }
    val template = DefaultGroovyTemplateFactory.Default.createTemplate(sourceProvider)
    val output = template.render()
    println(output)
}
