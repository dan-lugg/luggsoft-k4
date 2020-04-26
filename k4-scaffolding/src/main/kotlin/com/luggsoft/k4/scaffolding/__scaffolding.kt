package com.luggsoft.k4.scaffolding

import com.luggsoft.k4.core.Source
import com.luggsoft.k4.core.engine.Engine
import com.luggsoft.k4.scaffolding.internals.OBJECT_WRITER
import org.intellij.lang.annotations.Language
import java.io.Reader
import java.io.StringReader
import java.io.Writer
import javax.script.ScriptEngineManager

interface TemplateSourceReaderProvider
{
    fun getTemplateSourceReader(name: String): Reader
}

interface TemplateRenderer
{
    fun renderTemplate(name: String, model: Any?, writer: Writer)
}

class K4TemplateRenderer(
    private val k4Engine: Engine,
    private val templateSourceReaderProvider: TemplateSourceReaderProvider
) : TemplateRenderer
{
    override fun renderTemplate(name: String, model: Any?, writer: Writer)
    {
        val reader = this.templateSourceReaderProvider.getTemplateSourceReader(name)
        val source = Source.fromReader(name, reader)
        this.k4Engine.compileAndExecute(source, writer, model)
    }
}

///


@Language("")
const val SCRIPT = """
    import com.luggsoft.k4.scaffolding.ScaffoldDefinition
    
    ScaffoldDefinition(
        name = "Test"
    )
"""

fun main(vararg args: String)
{
    val script = SCRIPT.trimIndent()
    val scaffoldDefinitionProvider = DefaultScaffoldDefinitionProvider(
        scriptReader = StringReader(script),
        scriptEngine = ScriptEngineManager().getEngineByName("kotlin")
    )

    val scaffoldDefinition = scaffoldDefinitionProvider.getScaffoldDefinition()
    OBJECT_WRITER.writeValueAsString(scaffoldDefinition).also(::println)
}

