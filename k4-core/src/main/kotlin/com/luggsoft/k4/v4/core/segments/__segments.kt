package com.luggsoft.k4.v4.core.segments

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.github.h0tk3y.betterParse.combinators.AndCombinator
import com.github.h0tk3y.betterParse.combinators.and
import com.github.h0tk3y.betterParse.combinators.asJust
import com.github.h0tk3y.betterParse.combinators.map
import com.github.h0tk3y.betterParse.combinators.or
import com.github.h0tk3y.betterParse.combinators.separatedTerms
import com.github.h0tk3y.betterParse.combinators.skip
import com.github.h0tk3y.betterParse.grammar.Grammar
import com.github.h0tk3y.betterParse.grammar.parseToEnd
import com.github.h0tk3y.betterParse.lexer.Token
import com.github.h0tk3y.betterParse.lexer.TokenMatch
import com.github.h0tk3y.betterParse.lexer.literalToken
import com.github.h0tk3y.betterParse.lexer.regexToken
import com.github.h0tk3y.betterParse.lexer.token
import com.github.h0tk3y.betterParse.parser.Parser
import com.github.h0tk3y.betterParse.utils.Tuple2
import com.luggsoft.common.text.columnIndexAt
import com.luggsoft.common.text.lineIndexAt
import java.io.File
import java.io.Reader
import java.lang.StringBuilder
import java.nio.charset.Charset

interface Source : CharSequence
{
    val name: String
    val content: String

    companion object
}

fun Source.Companion.fromFile(file: File, charset: Charset = Charsets.UTF_8): Source = DefaultSource(
    name = file.absolutePath,
    content = file.readText(charset),
)

internal class DefaultSource(
    override val name: String,
    override val content: String,
) : Source
{
    override val length: Int
        get() = this.content.length

    override fun get(index: Int): Char = this.content[index]

    override fun subSequence(startIndex: Int, endIndex: Int): CharSequence = this.content.subSequence(startIndex, endIndex)
}

data class Location(
    @JsonIgnore
    val source: Source,

    @JsonProperty
    val startIndex: Int,

    @JsonProperty
    val untilIndex: Int,
) : Comparable<Location>
{
    @get:JsonProperty
    val startLineNumber: Int
        get() = this.source.lineIndexAt(this.startIndex) + 1

    @get:JsonProperty
    val untilLineNumber: Int
        get() = this.source.lineIndexAt(this.untilIndex) + 1

    @get:JsonProperty
    val startColumnNumber: Int
        get() = this.source.columnIndexAt(this.startIndex) + 1

    @get:JsonProperty
    val untilColumnNumber: Int
        get() = this.source.columnIndexAt(this.untilIndex) + 1

    override fun toString(): String = this.toStartCoordinateString()

    override fun compareTo(other: Location): Int
    {
        if (this.startIndex == other.startIndex)
        {
            if (this.untilIndex == other.untilIndex)
            {
                return 0
            }

            return this.untilIndex.compareTo(other.untilIndex)
        }

        return this.startIndex.compareTo(other.startIndex)
    }

    @JsonProperty
    fun toStartCoordinateString(): String = "${this.source.name}:${this.startLineNumber}:${this.startColumnNumber}"

    @JsonProperty
    fun toUntilCoordinateString(): String = "${this.source.name}:${this.untilLineNumber}:${this.untilColumnNumber}"
}

internal suspend inline fun <T> SequenceScope<T>.yield(provider: () -> T) = this.yield(provider())

internal val OBJECT_MAPPER = jacksonObjectMapper()

interface Segment
{
    @get:JsonProperty
    val content: String

    @get:JsonProperty
    val location: Location
}

internal fun buildSegmentTableString(segments: Iterable<Segment>): String
{
    val nameLength = segments
        .map { segment -> segment::class.simpleName!! }
        .maxOf(String::length)

    val coordinatesLength = segments
        .map { segment -> segment.location.toStartCoordinateString() }
        .maxOf(String::length)

    return buildString {
        for (segment in segments)
        {
            this.append(String.format("%-${nameLength}s", segment::class.simpleName))
            this.append(" | ")
            this.append(String.format("%-${coordinatesLength}s", segment.location))
            this.append(" | ")
            this.append(OBJECT_MAPPER.writeValueAsString(segment.content))
            this.append("\n")
        }
    }
}

data class RawSegment(
    override val content: String,
    override val location: Location,
) : Segment
{
    override fun toString(): String = "Raw: ${this.location}: ${OBJECT_MAPPER.writeValueAsString(this.content)}"
}

data class TagSegment(
    override val content: String,
    override val location: Location,
) : Segment
{
    override fun toString(): String = "Tag: ${this.location}: ${OBJECT_MAPPER.writeValueAsString(this.content)}"
}

interface SegmentParser
{
    fun parseSegments(source: Source, startIndex: Int = 0): List<Segment>
}

class DefaultSegmentParser(
    private val tagPrefix: String,
    private val tagSuffix: String,
) : SegmentParser
{
    override fun parseSegments(source: Source, startIndex: Int): List<Segment>
    {
        // Initialize parser state
        var index = startIndex
        var parseState = ParseState.RAW
        var segmentStartIndex = startIndex
        val segments = mutableListOf<Segment>()

        // While we're not at EOF
        while (index < source.length)
        {
            when (parseState)
            {
                // If we're parsing raw
                ParseState.RAW ->
                {
                    // If we see a tag coming
                    if (source.startsWith(tagPrefix, index))
                    {
                        // If there is raw content pending
                        if (index > segmentStartIndex)
                        {
                            // Add a [RawSegment] to the sequence
                            segments += RawSegment(
                                content = source.substring(
                                    startIndex = segmentStartIndex,
                                    endIndex = index
                                ),
                                location = Location(
                                    source = source,
                                    startIndex = segmentStartIndex,
                                    untilIndex = index - 1,
                                )
                            )

                            // Set the startIndex to after the [RawSegment]
                            segmentStartIndex = index
                        }

                        // Set the state to parse tags
                        parseState = ParseState.TAG
                    }
                }

                // If we're parsing tags
                ParseState.TAG ->
                {
                    // If we see the end of a tag coming
                    if (source.startsWith(tagSuffix, index))
                    {
                        // Add a [TagSegment] to the sequence
                        segments += TagSegment(
                            content = source.substring(
                                startIndex = segmentStartIndex,
                                endIndex = index + tagSuffix.length,
                            ),
                            location = Location(
                                source = source,
                                startIndex = segmentStartIndex,
                                untilIndex = index + tagSuffix.length - 1,
                            )
                        )

                        // Set the startIndex to after the [TagSegment]
                        // Set the state to parse raw
                        segmentStartIndex = index + tagSuffix.length
                        parseState = ParseState.RAW
                    }
                }
            }

            // Move forward
            index++
        }

        // If there's raw content pending
        if (index > segmentStartIndex)
        {
            // Add a [RawSegment] to the sequence
            segments += RawSegment(
                content = source.substring(segmentStartIndex, index),
                location = Location(
                    source = source,
                    startIndex = segmentStartIndex,
                    untilIndex = index + 1,
                )
            )
        }

        // Return all the segments
        return segments
    }

    private enum class ParseState
    {
        RAW,
        TAG,
    }
}

fun main()
{
    val source = Source.fromFile(
        file = File("./examples/example1.k4"),
    )

    /*
    source.forEachIndexed { index, char -> println("$index\t${OBJECT_MAPPER.writeValueAsString(char)}") }
    println()
    */

    val segmentParser = DefaultSegmentParser(
        tagPrefix = "<#",
        tagSuffix = "#>",
    )

    val segments = segmentParser.parseSegments(source)
    val segmentTable = buildSegmentTableString(segments)
    println(segmentTable)
    println()
}

/*
interface Value

data class ConstantValue(
    val value: Any?,
) : Value

data class VariableValue(
    val name: String,
) : Value

class TestGrammar : Grammar<Map<String, Value>>()
{
    private val whitespaceToken: Token by regexToken("\\s+", true)

    private val colonToken: Token by literalToken(":")

    private val commaToken: Token by literalToken(",")

    private val nullToken: Token by literalToken("null")

    private val trueBooleanToken: Token by literalToken("true")

    private val falseBooleanToken: Token by literalToken("false")

    private val stringToken: Token by regexToken("\"[^\\\\\"]*(\\\\[\"nrtbf\\\\][^\\\\\"]*)*\"")

    private val identifierToken: Token by regexToken("[a-zA-Z_][a-zA-Z0-9_]*")

    private val numberToken: Token by regexToken("\\d+(\\.\\d+)?")

    private val nullConstantValueParser: Parser<Value> by this.nullToken asJust ConstantValue(value = null)

    private val trueBooleanConstantValueParser: Parser<Value> by this.trueBooleanToken asJust ConstantValue(value = true)

    private val falseBooleanConstantValueParser: Parser<Value> by this.falseBooleanToken asJust ConstantValue(value = false)

    private val booleanConstantValueParser: Parser<Value> by this.trueBooleanConstantValueParser or this.falseBooleanConstantValueParser

    private val numberConstantValueParser: Parser<Value> by this.numberToken map number@{ tokenMatch ->
        return@number ConstantValue(
            value = tokenMatch.text.toIntOrNull()
                ?: tokenMatch.text.toLongOrNull()
                ?: tokenMatch.text.toFloatOrNull()
                ?: tokenMatch.text.toDoubleOrNull(),
        )
    }

    private val stringConstantValueParser: Parser<Value> by this.stringToken map string@{ tokenMatch ->
        return@string ConstantValue(
            value = tokenMatch.text.substring(
                startIndex = 1,
                endIndex = tokenMatch.text.lastIndex,
            )
        )
    }

    private val variableValueParser: Parser<Value> by this.identifierToken map { tokenMatch -> VariableValue(tokenMatch.text) }

    private val nameParser: Parser<String> by this.identifierToken map (TokenMatch::text)

    private val valueParser: Parser<Value> by or(
        this.nullConstantValueParser,
        this.booleanConstantValueParser,
        this.numberConstantValueParser,
        this.stringConstantValueParser,
        this.variableValueParser,
    )

    private val pairParser: Parser<Pair<String, Value>> by this.nameParser and skip(this.colonToken) and this.valueParser map { (name, value) -> name to value }

    private val mapParser: Parser<Map<String, Value>> by separatedTerms(this.pairParser, this.commaToken) map { pairs -> pairs.toMap() }

    ///

    override val rootParser: Parser<Map<String, Value>> by this.mapParser

    ///

    private fun <T> or(vararg p: Parser<T>): Parser<T> = p.reduce { orParser, parser -> orParser or parser }

    // public inline infix fun <reified A, reified B> Parser<A>.and(other: Parser<B>): AndCombinator<Tuple2<A, B>> =
    //     AndCombinator(listOf(this, other)) { (a1, a2) -> Tuple2(a1 as A, a2 as B) }

}
*/

