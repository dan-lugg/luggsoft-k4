package com.luggsoft.k4.core

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.h0tk3y.betterParse.combinators.and
import com.github.h0tk3y.betterParse.combinators.map
import com.github.h0tk3y.betterParse.combinators.or
import com.github.h0tk3y.betterParse.combinators.separatedTerms
import com.github.h0tk3y.betterParse.combinators.skip
import com.github.h0tk3y.betterParse.grammar.Grammar
import com.github.h0tk3y.betterParse.grammar.parseToEnd
import com.github.h0tk3y.betterParse.lexer.RegexToken
import com.github.h0tk3y.betterParse.lexer.literalToken
import com.github.h0tk3y.betterParse.lexer.regexToken
import com.github.h0tk3y.betterParse.parser.Parser
import com.luggsoft.k4.core.segments.MetaTagSegment
import com.luggsoft.k4.core.segments.Segment
import com.luggsoft.k4.core.segments.parsers.DefaultSegmentParser
import com.luggsoft.k4.core.segments.parsers.SegmentParser
import com.luggsoft.k4.core.sources.Source
import com.luggsoft.k4.core.sources.StringSource
import org.intellij.lang.annotations.Language
import java.io.File
import kotlin.reflect.KClass

@Target(AnnotationTarget.EXPRESSION)
@Retention(AnnotationRetention.SOURCE)
annotation class LocationInfo(
    val sourceName: String,
    val startIndex: Int,
    val untilIndex: Int,
)

data class TemplateData(
    val source: Source,
    val segments: Iterable<Segment>,
    val modelName: String,
    val modelKClass: KClass<*>,
)

interface TemplateDataParser
{
    fun parseTemplateData(source: Source): TemplateData
}

class DefaultTemplateDataParser(
    private val segmentParser: SegmentParser,
    private val metadataGrammar: MetadataGrammar,
) : TemplateDataParser
{
    override fun parseTemplateData(source: Source): TemplateData
    {
        val segments = this.segmentParser.parseSegments(source)
        segments.forEach(::println)
        TODO()

        val metadataMap = segments
            .filterIsInstance<MetaTagSegment>()
            .map(this::parseMetadata)
            .flatMap(Map<String, Any?>::entries)
            .associate(Map.Entry<String, Any?>::toPair)
            .toMap()

        println(metadataMap)
        segments.forEach(::println)
        TODO()
    }

    private fun parseMetadata(metaTagSegment: MetaTagSegment): Map<String, Any?> = this.metadataGrammar
        .parseToEnd(metaTagSegment.content)
}

///

class MetadataGrammar(
    private val objectMapper: ObjectMapper,
    private val valueKClassMapping: Map<String, KClass<*>>,
) : Grammar<Map<String, Any?>>()
{
    val spaceToken by expressionToken(pattern = """\s+""", ignore = true)

    val colonToken by literalToken(text = ":")
    val commaToken by literalToken(text = ",")

    val trueBooleanValueToken by literalToken(text = "true")

    val falseBooleanValueToken by literalToken(text = "false")

    val booleanValueToken by or(
        this.trueBooleanValueToken,
        this.falseBooleanValueToken
    )

    val nullValueToken by literalToken(text = "null")

    val nameToken by expressionToken(pattern = """[a-zA-Z_][a-zA-Z0-9_]*""")

    val stringValueToken by expressionToken(pattern = """"((\\"|[^"])*)"""")

    val numberValueToken by expressionToken(pattern = """-?([1-9][0-9]*|0)(\.[0-9]+)?""")

    val valueToken by or(
        this.stringValueToken,
        this.numberValueToken,
        this.booleanValueToken,
        this.nullValueToken,
    )

    val pairParser: Parser<Pair<String, Any?>> by (this.nameToken and skip(this.colonToken) and this.valueToken) map pair@{ (nameTokenMatch, valueTokenMatch) ->
        val valueKClass = this.valueKClassMapping.getOrDefault(nameTokenMatch.text, Any::class)
        return@pair nameTokenMatch.text to this.objectMapper.readValue(valueTokenMatch.text, valueKClass.java)
    }

    override val rootParser: Parser<Map<String, Any?>> by separatedTerms(this.pairParser, this.commaToken, true) map map@{ pairs -> pairs.toMap() }

    private companion object
    {
        fun expressionToken(@Language("RegExp") pattern: String, ignore: Boolean = false): RegexToken = regexToken(
            pattern = pattern,
            ignore = ignore,
        )

        fun <T> or(vararg parsers: Parser<T>): Parser<T> = parsers.reduce { orParser, parser -> orParser or parser }
    }
}

///

const val INPUT = """
<#@ modelName: "foo", modelType: "java.lang.String" #>
Hello <#= foo #>!
"""

fun main()
{
    /*
    val objectMapper = jacksonObjectMapper()

    val templateDataParser = DefaultTemplateDataParser(
        segmentParser = DefaultSegmentParser(),
        metadataGrammar = MetadataGrammar(
            objectMapper = objectMapper,
            valueKClassMapping = mapOf(
                "modelName" to String::class,
                "modelType" to Class::class,
            )
        )
    )

    val source = Source.fromFile(
        file = File("/Users/dan.lugg/IdeaProjects/kt-k4/k4-core/src/main/resources/example.k4"),
    )

    templateDataParser.parseTemplateData(source)
    */

    val file = File("/Users/dan.lugg/IdeaProjects/kt-k4/k4-core/src/main/resources/example.k4")

    val segmentParser = DefaultSegmentParser()
    val source = StringSource(
        name = file.absolutePath,
        content = file.readText(),
    )
    val segments = segmentParser.parseSegments(source)
    segments.forEach(::println)
}
