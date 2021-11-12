package com.luggsoft.k4.core

import com.luggsoft.k4.core.sources.DefaultSource
import org.intellij.lang.annotations.Language
import org.slf4j.LoggerFactory
import java.io.StringWriter

///

///

data class ModelX(val a: Int, val b: Int)

data class ModelY(val a: String, val b: String)

val sourceContent = """
<#@
  model-type: com.luggsoft.k4.core.ModelY
#>
<#= model.a #> + <#= model.b #> = <#= model.a + model.b #>
"""

@Language("yaml")
val modelYaml = """
foo:
  a: 123
  b: 234
bar:
  a: 345
  b: 456
qux:
  a: "Hello"
  b: "World"
"""

@Language("K4")
val eventK4 = """
<#@ modelType: com.luggsoft.modelgen.EventDescriptor #>
package com.example.models

data class <#= model.name #>Event(
    val id: UUID,
    val at: Instance,
    val data: <#= model.name #>EventData, 
) : Event<<#= model.name #>EventData>

data class <#= model.name #>EventData(
<#! model.properties.forEach { property -> #>
    val <#= property.name #>: <#= property.type #>,
<#! } #>
) : EventData

"""

fun main()
{
    val source = DefaultSource(
        name = "example",
        content = sourceContent.trimStart(),
    )

    val modelProvider = DefaultModelProvider(
        modelNameMap = mapOf(
            "foo" to ModelX(
                a = 123,
                b = 456,
            ),
            "bar" to ModelX(
                a = 456,
                b = 789,
            ),
            "qux" to ModelY(
                a = "Goodbye",
                b = "Universe",
            )
        ),
    )

    val writer = StringWriter()
    val logger = LoggerFactory.getLogger("test")

    DefaultEngine.Instance.execute(
        source = source,
        modelProvider = modelProvider,
        modelName = "qux",
        writer = writer,
        logger = logger,
    )

    writer.flush()
    writer.toString().trim().also(::println)
}

///

/*
1) Source to Segments
2)

*/
