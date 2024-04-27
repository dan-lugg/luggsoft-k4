package com.luggsoft.k4.core

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.github.h0tk3y.betterParse.combinators.and
import com.github.h0tk3y.betterParse.combinators.map
import com.github.h0tk3y.betterParse.combinators.skip
import com.github.h0tk3y.betterParse.combinators.zeroOrMore
import com.github.h0tk3y.betterParse.grammar.Grammar
import com.github.h0tk3y.betterParse.grammar.parseToEnd
import com.github.h0tk3y.betterParse.lexer.RegexToken
import com.github.h0tk3y.betterParse.lexer.literalToken
import com.github.h0tk3y.betterParse.lexer.regexToken
import com.github.h0tk3y.betterParse.parser.Parser
import com.luggsoft.k4.core.segments.MetaTagSegment
import com.luggsoft.k4.core.segments.Segment
import com.luggsoft.k4.core.segments.parsers.DefaultSegmentParser
import com.luggsoft.k4.core.sources.Source
import org.intellij.lang.annotations.Language
import org.slf4j.LoggerFactory
import java.io.File
import java.io.Writer
import kotlin.reflect.KClass

interface MetaPropertyParser
{
    fun parseMetaProperty(input: String): MetaProperty
}

class DefaultMetaPropertyParser(
    private val metaPropertyGrammar: MetaPropertyGrammar,
) : MetaPropertyParser
{
    override fun parseMetaProperty(input: String): MetaProperty = this.metaPropertyGrammar
        .parseToEnd(input)
}

interface TemplateFactory
{
    fun <TModel : Any> createTemplate(segments: Iterable<Segment>): Template<TModel>
}

internal fun Iterable<MetaProperty>.ofName(metaDirectiveName: String): Iterable<MetaProperty> = this.asSequence()
    .filter { metaProperty -> metaProperty.name.equals(metaDirectiveName, true) }
    .toList()

internal fun Iterable<MetaProperty>.asMap(metaDirectiveName: String): Map<String, Any?> = this.ofName(metaDirectiveName)
    .map { metaProperty -> metaProperty.attributeMap }
    .reduce { acc, map -> acc + map }

internal fun Iterable<MetaProperty>.asList(metaDirectiveName: String, attributeName: String): List<Any?> = this.ofName(metaDirectiveName)
    .map { metaProperty -> metaProperty.attributeMap[attributeName] }
    .toList()

class DefaultTemplateFactory(
    private val metaPropertyParser: MetaPropertyParser,
) : TemplateFactory
{
    private fun buildImports(metaProperties: List<MetaProperty>): List<Class<*>> = metaProperties.asSequence()
        .filter { metaProperty -> metaProperty.name == "import" }
        .mapNotNull { metaProperty -> metaProperty.attributeMap["name"] as? String }
        .map { name -> Class.forName(name) }
        .toList()

    override fun <TModel : Any> createTemplate(segments: Iterable<Segment>): Template<TModel>
    {
        val metaProperties = this.parseMetaProperties(segments)
        println(metaProperties)

        val importData = metaProperties.asList("import", "name")
        println(importData)

        val templateData = metaProperties.asMap("template")
        println(templateData)

        /*
        val imports = this.buildImports(metaProperties)
        for (import in imports)
        {
            println(import.name)
        }

        println(imports)
        */

        TODO()
    }

    private fun generateTemplate(segments: Iterable<Segment>, writer: Writer)
    {

    }

    private fun generateTemplateHeader(segments: Iterable<Segment>, writer: Writer)
    {
    }

    private fun generateTemplateScript(segments: Iterable<Segment>, writer: Writer)
    {

    }

    private fun generateTemplateFooter(segments: Iterable<Segment>, writer: Writer)
    {
    }

    private fun parseMetaProperties(segments: Iterable<Segment>) = segments
        .asSequence()
        .filterIsInstance<MetaTagSegment>()
        .map(MetaTagSegment::content)
        .map(this.metaPropertyParser::parseMetaProperty)
        .toList()

}

///

data class MetaProperty(
    val name: String,
    val attributeMap: Map<String, Any?>,
)

class MetaPropertyGrammar(
    private val objectMapper: ObjectMapper,
    private val attributeKClassMapping: Map<String, Map<String, KClass<*>>>,
) : Grammar<MetaProperty>()
{
    val wsToken by expressionToken(pattern = """\s+""", ignore = true)

    val nameToken by expressionToken(pattern = """\w+""")

    val valueToken by expressionToken(pattern = """"([^"]|\\")*"""")

    val equalToken by literalToken(text = "=")

    val attributePairParser: Parser<Pair<String, String>> by (this.nameToken and skip(this.equalToken) and this.valueToken) map attributePair@{ (nameTokenMatch, valueTokenMatch) ->
        return@attributePair nameTokenMatch.text to valueTokenMatch.text
    }

    val attributeMapParser: Parser<Map<String, String>> by zeroOrMore(this.attributePairParser) map attributeMap@{ attributePairs ->
        return@attributeMap attributePairs.toMap()
    }

    override val rootParser: Parser<MetaProperty> by (this.nameToken and this.attributeMapParser) map root@{ (nameTokenMatch, attributeMap) ->
        val name = nameTokenMatch.text
        return@root MetaProperty(
            name = name,
            attributeMap = attributeMap.mapValues { (key, value) ->
                val kClass = this.attributeKClassMapping.get(name)?.get(key) ?: Any::class
                return@mapValues this.objectMapper.readValue(value, kClass.java)
            },
        )
    }

    private companion object
    {
        fun expressionToken(@Language("RegExp") pattern: String, ignore: Boolean = false): RegexToken = regexToken(
            pattern = pattern,
            ignore = ignore,
        )
    }
}

/*
fun main()
{
    val metadataGrammar = MetadataGrammar(
        objectMapper = jacksonObjectMapper(),
        attributeKClassMapping = mapOf(
            "modelName" to String::class,
            "modelType" to Class::class,
            "recursion" to Int::class,
        ),
    )

    val directive = metadataGrammar.parseToEnd(""" myDirective """)
    println(directive)
    for ((key, value) in directive.attributeMap)
    {
        println("$key -> $value (${(value ?: Unit)::class})")
    }
}
*/

fun main()
{
    val source = Source.fromFile(
        file = File("${File("./").canonicalPath}/k4-core/src/main/resources/example.k4"),
    )

    val segmentParser = DefaultSegmentParser(
        logger = LoggerFactory.getLogger(DefaultSegmentParser::class.java),
    )

    val templateFactory = DefaultTemplateFactory(
        metaPropertyParser = DefaultMetaPropertyParser(
            metaPropertyGrammar = MetaPropertyGrammar(
                objectMapper = jacksonObjectMapper(),
                attributeKClassMapping = emptyMap(),
            )
        )
    )

    val writer = System.out.writer()

    val segments = segmentParser.parseSegments(source)
    val template = templateFactory.createTemplate<Any>(segments)
    template.render(writer, null)
    writer.flush()

    ///

    /*
    val segmentParser = DefaultSegmentParser(
        logger = LoggerFactory.getLogger("parser"),
    )

    val source = FileSource(
        file = File("/Users/dan.lugg/IdeaProjects/kt-k4/k4-core/src/main/resources/example.k4"),
    )

    val segments = segmentParser.parseSegments(source)

    val objectMapper = jacksonObjectMapper()

    val metadataGrammar = MetadataGrammar(
        objectMapper = objectMapper,
        valueKClassMapping = emptyMap(),
    )

    val metadataMap = segments.asSequence()
        .takeWhile { segment ->
            return@takeWhile when (segment)
            {
                is MetaTagSegment -> true
                is RawSegment -> segment.content.isBlank()
                else -> false
            }
        }
        .filterIsInstance<MetaTagSegment>()
        .map(MetaTagSegment::content)
        .map(metadataGrammar::parseToEnd)
        .flatMap(Map<String, *>::entries)
        .associate(Map.Entry<String, *>::toPair)

    println("metadataMap=$metadataMap")
    println()

    for (segment in segments)
    {
        println("segment=$segment")
        println()
    }
    */

    ///

    /*
    val string = "Hello, how are you doing today?"
    val reader = StringReader(string)
    val iterator = ReadableIterator(reader, 8)
    val sourceIterator = DefaultSourceIterator(iterator)

    while (sourceIterator.hasNext())
    {
        if (sourceIterator.peekEquals("you"))
        {
            sourceIterator.skip(3)
            continue
        }

        val char = sourceIterator.next()
        print(char)
    }
    */
}

///

