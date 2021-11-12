package com.luggsoft.k4.v4.core

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import java.io.File
import java.io.FileNotFoundException
import java.nio.charset.Charset
import java.net.URL as Url

interface Source : CharSequence
{
    val name: String
    val content: String

    companion object
}

internal data class DefaultSource(
    override val name: String,
    override val content: String,
) : Source
{
    override val length: Int
        get() = this.content.length

    override fun get(index: Int): Char = this.content.get(index)

    override fun subSequence(startIndex: Int, endIndex: Int): CharSequence = this.content.subSequence(startIndex, endIndex)
}

fun Source.Companion.fromUrl(url: Url, charset: Charset = Charsets.UTF_8): Source = DefaultSource(
    name = url.toString(),
    content = url.readText(charset),
)

fun Source.Companion.fromFile(file: File, charset: Charset = Charsets.UTF_8): Source = DefaultSource(
    name = file.absolutePath,
    content = file.readText(charset),
)

fun Source.Companion.fromString(name: String, content: String): Source = DefaultSource(
    name = name,
    content = content,
)

class Location(
    val startIndex: Int,
    val untilIndex: Int,
)

interface Segment
{
    val content: String
    val location: Location
}

data class RawSegment(
    override val content: String,
    override val location: Location,
) : Segment

data class TagSegment(
    override val content: String,
    override val location: Location,
) : Segment
{
    val tagType: TagType
        get() = when (this.content[2])
        {
            '@' -> TagType.META
            '=' -> TagType.ECHO
            '+' -> TagType.HELPER
            '*' -> TagType.COMMENT
            else -> TagType.CODE
        }

    val value: String
        get() = this.content
            .drop(if (this.tagType == TagType.CODE) 2 else 3)
            .dropLast(2)
            .trim()
}

enum class TagType
{
    CODE,
    META,
    ECHO,
    HELPER,
    COMMENT,
}

interface SegmentParser<T : Segment>
{
    fun parseSegments(source: Source, startIndex: Int = 0): List<T>
}

class DefaultSegmentParser : SegmentParser<Segment>
{
    override fun parseSegments(source: Source, startIndex: Int): List<Segment>
    {
        var index = startIndex
        val tagBuffer = StringBuilder()
        val rawBuffer = StringBuilder()
        val segments = mutableListOf<Segment>()

        while (index < source.length)
        {
            if (source.startsWith("<#", index))
            {
                if (rawBuffer.isNotEmpty())
                {
                    segments += RawSegment(
                        content = rawBuffer.toString(),
                        location = Location(
                            startIndex = index - rawBuffer.length,
                            untilIndex = index - 1,
                        )
                    )

                    rawBuffer.clear()
                }

                while (index < source.length)
                {
                    if (source.startsWith("\"\"\"", index))
                    {
                        while (index < source.length)
                        {
                            tagBuffer.append(source[index++])

                            if (tagBuffer.endsWith("\"\"\""))
                            {
                                break
                            }
                        }
                    }

                    if (source.startsWith("\"", index))
                    {
                        while (index < source.length)
                        {
                            tagBuffer.append(source[index++])

                            if (tagBuffer.endsWith("\\\""))
                            {
                                continue
                            }

                            if (tagBuffer.endsWith("\""))
                            {
                                break
                            }
                        }
                    }

                    if (tagBuffer.endsWith("#>"))
                    {
                        segments += TagSegment(
                            content = tagBuffer.toString(),
                            location = Location(
                                startIndex = index - tagBuffer.length,
                                untilIndex = index - 1,
                            )
                        )

                        tagBuffer.clear()
                        break
                    }

                    tagBuffer.append(source[index++])
                }

                continue
            }

            rawBuffer.append(source[index++])
        }

        if (rawBuffer.isNotEmpty())
        {
            segments += RawSegment(
                content = rawBuffer.toString(),
                location = Location(
                    startIndex = index - rawBuffer.length,
                    untilIndex = index - 1,
                )
            )

            rawBuffer.clear()
        }

        return segments
    }
}

///

interface Instruction

data class TextInstruction(
    val text: String,
) : Instruction

data class CodeInstruction(
    val code: String,
) : Instruction

data class CallInstruction(
    val name: String,
    val modelName: String,
) : Instruction

///

fun main()
{
    lateinit var file: File
    lateinit var source: Source

    try
    {
        file = File("./examples/example1.k4")
        source = Source.fromFile(
            file = file
        )
    } catch (exception: FileNotFoundException)
    {
        throw Exception(file.absolutePath, exception)
    }

    val segmentParser = DefaultSegmentParser()
    val segments = segmentParser.parseSegments(source)

    val objectMapper = jacksonObjectMapper()
    segments.forEach { segment ->
        buildString {
            append(" ${String.format("%-15s", segment::class.simpleName)}|")
            append(" ${String.format("%-7s", "${segment.location.startIndex},${segment.location.untilIndex}")}|")
            append(" ${objectMapper.writeValueAsString(source.substring(segment.location.startIndex, segment.location.untilIndex + 1))}")
            if (segment is TagSegment)
            {
                append(" ${segment.tagType} (${objectMapper.writeValueAsString(segment.value)})")
            }
        }.also(::println)
    }
}
