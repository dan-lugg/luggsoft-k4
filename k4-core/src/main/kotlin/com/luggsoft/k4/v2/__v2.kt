package com.luggsoft.k4.v2

import com.luggsoft.k4.v2.templates.kotlinEscape

val content = """
<#@
model-type: com.luggsoft.k4.v2.ExampleModel
#>
Raw line 1.
<#= 123 #> 
Raw line 2.
<#! if (true) { #>
<#*
Some comments.
More comments.
#>
Raw line 3.
Hello <#= model.name #>! How are you doing today?
You won ${'$'}DOLLARS!
<#! } #>
Raw line 4.
"""

data class ExampleModel(
    val name: String,
)

fun main()
{
    /*
    val source = DefaultSource(
        name = "test",
        content = content.trimStart()
    )

    val engine = DefaultEngine(
        sourceParser = DefaultSourceParser(
            sourceParserSettings = SourceParserSettings()
                .enable(SourceParserFlags.SKIP_TAG_TRAILING_NEWLINES),
        ),
        templateGenerator = DefaultTemplateGenerator(
            objectMapper = jacksonObjectMapper(),
            scriptEngineManager = ScriptEngineManager(),
            templateBuilderSettings = TemplateBuilderSettings()
                .enable(TemplateBuilderFlags.INCLUDE_COMMENTS_INLINE)
                .enable(TemplateBuilderFlags.INCLUDE_COMMENTS_LOGGED),
        ),
    )

    val writer = System.out.writer()
    val template = engine.compile(
        source = source,
    )

    template.save(writer)

    val exampleModels = ('A'..'Z').map { char ->
        return@map ExampleModel(name = "$char")
    }

    for (exampleModel in exampleModels)
    {
        template.execute(
            model = exampleModel,
            writer = writer,
        )
    }

    writer.flush()
    */
    val x = """
        asdf
        "asdf"${'$'}asdf
        asdf
    """
    x.kotlinEscape().also(::println)
    "\n        asdf\n        \"asdf\"\$asdf\n        asdf\n"
}

