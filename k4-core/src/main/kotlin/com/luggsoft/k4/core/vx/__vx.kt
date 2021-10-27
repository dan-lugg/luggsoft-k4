package com.luggsoft.k4.core.vx

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.luggsoft.k4.core.vx.internal.formatters.DefaultFormatter
import com.luggsoft.k4.core.vx.internal.formatters.Formatter
import com.luggsoft.k4.core.vx.internal.tokenizers.DefaultTokenizer
import com.luggsoft.k4.core.vx.internal.tokenizers.Tokenizer
import com.luggsoft.k4.core.vx.internal.generators.DefaultGenerator
import com.luggsoft.k4.core.vx.internal.generators.Generator
import com.luggsoft.k4.core.vx.internal.generators.GeneratorFlags
import com.luggsoft.k4.core.vx.io.FileSource
import com.luggsoft.k4.core.vx.io.Source
import org.slf4j.Logger
import java.io.Reader
import java.io.StringWriter
import java.io.Writer
import java.util.*
import javax.script.Invocable
import javax.script.ScriptEngineManager
import kotlin.system.measureTimeMillis

val INPUT = """
<#@
    modelType: ${com.pinterest.ktlint.core.Rule::class.qualifiedName}
    packageName: com.luggsoft.k4.templates
    importsNames:
        - com.luggsoft.k4.core.vx.MyImport1
        - com.luggsoft.k4.core.vx.MyImport2
#>
<#!
    val x = 123 
    val y = 456 
    val a = 'a' 
    val b = 'b' 
    val c = 'c'
#>
Raw 1.
Raw 2 <#= "<#! if (x) \"1\" else \"2\" #>".padEnd(123) #><#= y #>, continued.
Raw 3.
<#! if (true) { #>Raw 4 <#= a #><#= b #>, continued.<#! } #>
<#= "asdf" #>
"""

fun main()
{
    val writer = System.out.writer()
    val source = FileSource(
        pathname = "C:\\Users\\dan.lugg\\IdeaProjects\\kt-k4\\k4-core\\src\\main\\resources\\example.k4",
    )
    jacksonObjectMapper().writeValueAsString(source).also(::println)

    val millis = measureTimeMillis {
        val tokens = DefaultTokenizer.Instance.tokenize(source, 0)
        val generator = DefaultGenerator(
            objectMapper = jacksonObjectMapper(),
            generatorFlags = GeneratorFlags()
                .enable(GeneratorFlags.Flag.INCLUDE_COMMENTS)
                .enable(GeneratorFlags.Flag.INCLUDE_SOURCE_MARKERS),
            metaParser = DefaultMetaParser.Instance,
            formatter = DefaultFormatter.Instance,
        )
        generator.generate(tokens, writer)
    }

    writer.flush()
    println("Done (length=${source.length}, time=${millis}ms)")
}

///

interface EngineFacade
{
    fun compile(source: Source): Template

    fun execute(source: Source): Unit
}

class DefaultEngineFacade(
    private val tokenizer: Tokenizer,
    private val generator: Generator,
) : EngineFacade
{
    override fun compile(source: Source): Template
    {
        val writer = StringWriter()
        val tokens = this.tokenizer.tokenize(source)
        this.generator.generate(tokens, writer)
        TODO()
    }

    override fun execute(source: Source)
    {
        val template = this.compile(source)
        TODO("Not yet implemented")
    }
}

interface Template
{
    fun render(model: Any?, writer: Writer, logger: Logger)
}

interface Script
{
    val reader: Reader
}

class ScriptTemplate(
    private val script: Script,
    private val scriptEngineManager: ScriptEngineManager,
) : Template
{
    override fun render(model: Any?, writer: Writer, logger: Logger)
    {
        val scriptEngine = this.scriptEngineManager.getEngineByName(SCRIPT_ENGINE_NAME)
        scriptEngine.eval(this.script.reader)
        val invocable = scriptEngine as Invocable
        invocable.invokeFunction("render", model, writer, logger)
    }
}

///

class Engine(
    val tokenizer: Tokenizer,
    val generator: Generator,
    val formatter: Formatter,
    val templater: Templater,
)

interface Templater

///

// tokenReader
// outputWriter
// templateFactory
