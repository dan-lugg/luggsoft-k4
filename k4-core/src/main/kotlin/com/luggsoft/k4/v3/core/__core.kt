package com.luggsoft.k4.v3.core

import com.luggsoft.common.text.columnIndexAt
import com.luggsoft.common.text.lineIndexAt
import com.luggsoft.k4.core.templates.kotlinEscape
import org.intellij.lang.annotations.Language
import java.io.File
import java.io.FileNotFoundException
import java.io.Writer
import java.nio.charset.Charset
import javax.script.Invocable
import javax.script.ScriptContext
import javax.script.ScriptEngine
import javax.script.ScriptEngineManager
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance

data class Source(
    val name: String,
    val content: String,
)
{
    companion object
}

fun Source.Companion.fromFile(file: File, charset: Charset = Charsets.UTF_8): Source = Source(
    name = file.absolutePath,
    content = file.readText(charset)
)

data class Location(
    val source: Source,
    val startIndex: Int,
    val untilIndex: Int,
) : Comparable<Location>
{
    val startLineNumber: Int
        get() = this.source.content.lineIndexAt(this.startIndex) + 1

    val untilLineNumber: Int
        get() = this.source.content.lineIndexAt(this.untilIndex) + 1

    val startColumnNumber: Int
        get() = this.source.content.columnIndexAt(this.startIndex) + 1

    val untilColumnNumber: Int
        get() = this.source.content.columnIndexAt(this.untilIndex) + 1

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
}

interface Segment
{
    val content: String
    val location: Location
}

data class RawSegment(
    override val content: String,
    override val location: Location,
) : Segment

interface TagSegment : Segment

val TagSegment.trimmedContent: String
    get() = this.content.lines()
        .joinToString(separator = "\n", transform = String::trim)

data class MetaTagSegment(
    override val content: String,
    override val location: Location,
) : TagSegment

data class BlockTagSegment(
    override val content: String,
    override val location: Location,
) : TagSegment

data class PrintTagSegment(
    override val content: String,
    override val location: Location,
) : TagSegment

data class IncludeTagSegment(
    override val content: String,
    override val location: Location,
) : TagSegment

data class CommentTagSegment(
    override val content: String,
    override val location: Location,
) : TagSegment

class SourceLoader
{
    fun loadSource(name: String): Source
    {
        lateinit var file: File

        try
        {
            file = File(name)
            return Source(
                name = file.absolutePath,
                content = file.readText(),
            )
        } catch (exception: FileNotFoundException)
        {
            throw Exception("File not found: ${file.absolutePath}")
        }
    }
}

class Meta

data class ParsedSegments(
    val meta: Meta,
    val segments: List<Segment>,
) : Iterable<Segment>
{
    override fun iterator(): Iterator<Segment> = this.segments.iterator()
}

fun <E> buildList(builder: MutableList<E>.() -> Unit): List<E> = mutableListOf<E>()
    .also(builder)
    .toList()

class SegmentParser(
    private val sourceLoader: SourceLoader,
    private val tagSegmentParsers: List<TagSegmentParser<*>>,
)
{
    constructor() : this(
        sourceLoader = SourceLoader(),
        tagSegmentParsers = listOf(
            MetaTagSegmentParser(allowMissingSuffix = false),
            BlockTagSegmentParser(allowMissingSuffix = true),
            PrintTagSegmentParser(allowMissingSuffix = false),
            IncludeTagSegmentParser(allowMissingSuffix = false),
            CommentTagSegmentParser(allowMissingSuffix = false),
        ),
    )

    fun parseSegments(source: Source): ParsedSegments
    {
        return this.parseSegmentsDirectly(source)
            .let(this::recurseIncludeTagSegments)
            .let(this::createParsedSegments)
    }

    private fun parseSegmentsDirectly(source: Source): List<Segment>
    {
        var index = 0
        val buffer = StringBuilder()
        val resultSegments = mutableListOf<Segment>()

        while (index < source.content.length)
        {
            var tagSegment: Segment? = null

            for (tagSegmentParser in this@SegmentParser.tagSegmentParsers)
            {
                tagSegment = tagSegmentParser.parseTagSegment(source, index)
                    ?: continue

                if (buffer.isNotEmpty())
                {
                    val rawSegment = RawSegment(
                        content = buffer.toString(),
                        location = Location(
                            source = source,
                            startIndex = index - buffer.length,
                            untilIndex = index - 1,
                        )
                    )

                    resultSegments.add(rawSegment)
                    buffer.clear()
                }

                index = tagSegment.location.untilIndex + 1
                resultSegments.add(tagSegment)
                break
            }

            if (tagSegment == null)
            {
                buffer.append(source.content[index++])
            }
        }

        if (buffer.isNotEmpty())
        {
            val rawSegment = RawSegment(
                content = buffer.toString(),
                location = Location(
                    source = source,
                    startIndex = index - buffer.length,
                    untilIndex = index - 1,
                )
            )

            resultSegments.add(rawSegment)
            buffer.clear()
        }

        return this.recurseIncludeTagSegments(resultSegments)
    }

    private fun recurseIncludeTagSegments(segments: List<Segment>): List<Segment>
    {
        val resultSegments = mutableListOf<Segment>()

        for (segment in segments)
        {
            if (segment is IncludeTagSegment)
            {
                val includeSource = this.sourceLoader.loadSource(segment.trimmedContent)
                val includeParsedSegments = this.parseSegmentsDirectly(includeSource)
                resultSegments.addAll(includeParsedSegments)
                continue
            }

            resultSegments.add(segment)
        }

        return resultSegments
    }

    private fun createParsedSegments(segments: List<Segment>): ParsedSegments
    {
        for (segment in segments)
        {
            if (segment is MetaTagSegment)
            {
                TODO()
            }
        }

        return ParsedSegments(
            meta = Meta(),
            segments = segments.filterNot { segment -> segment is MetaTagSegment }
        )
    }
}

interface TagSegmentParser<T : TagSegment>
{
    fun parseTagSegment(source: Source, startIndex: Int): T?
}

abstract class TagSegmentParserBase<T : TagSegment> : TagSegmentParser<T>
{
    abstract val allowMissingSuffix: Boolean

    protected abstract val tagPrefix: String
    protected abstract val tagSuffix: String

    final override fun parseTagSegment(source: Source, startIndex: Int): T?
    {
        if (source.content.startsWith(this.tagPrefix, startIndex))
        {
            var index = startIndex
            val buffer = StringBuilder()

            while (index < source.content.length)
            {
                buffer.append(source.content[index++])

                if (buffer.endsWith(this.tagSuffix))
                {
                    return this.createTagSegment(
                        content = buffer.toString()
                            .drop(this.tagPrefix.length)
                            .dropLast(this.tagSuffix.length)
                            .trim(),
                        location = Location(
                            source = source,
                            startIndex = startIndex,
                            untilIndex = startIndex + buffer.length - 1,
                        ),
                    )
                }
            }

            if (this.allowMissingSuffix && buffer.isNotBlank())
            {
                return this.createTagSegment(
                    content = buffer.toString()
                        .drop(this.tagPrefix.length)
                        .trim(),
                    location = Location(
                        source = source,
                        startIndex = startIndex,
                        untilIndex = startIndex + buffer.length - 1,
                    ),
                )
            }
        }

        return null
    }

    protected abstract fun createTagSegment(content: String, location: Location): T
}

class MetaTagSegmentParser(
    override val allowMissingSuffix: Boolean,
) : TagSegmentParserBase<MetaTagSegment>()
{
    override val tagPrefix: String = "<#@"
    override val tagSuffix: String = "#>"

    override fun createTagSegment(content: String, location: Location): MetaTagSegment = MetaTagSegment(
        content = content,
        location = location,
    )
}

class BlockTagSegmentParser(
    override val allowMissingSuffix: Boolean,
) : TagSegmentParserBase<BlockTagSegment>()
{
    override val tagPrefix: String = "<#!"
    override val tagSuffix: String = "#>"

    override fun createTagSegment(content: String, location: Location): BlockTagSegment = BlockTagSegment(
        content = content,
        location = location,
    )
}

class PrintTagSegmentParser(
    override val allowMissingSuffix: Boolean,
) : TagSegmentParserBase<PrintTagSegment>()
{
    override val tagPrefix: String = "<#="
    override val tagSuffix: String = "#>"

    override fun createTagSegment(content: String, location: Location): PrintTagSegment = PrintTagSegment(
        content = content,
        location = location,
    )
}

class IncludeTagSegmentParser(
    override val allowMissingSuffix: Boolean,
) : TagSegmentParserBase<IncludeTagSegment>()
{
    override val tagPrefix: String = "<#&"
    override val tagSuffix: String = "#>"

    override fun createTagSegment(content: String, location: Location): IncludeTagSegment = IncludeTagSegment(
        content = content,
        location = location,
    )
}

class CommentTagSegmentParser(
    override val allowMissingSuffix: Boolean,
) : TagSegmentParserBase<CommentTagSegment>()
{
    override val tagPrefix: String = "<#*"
    override val tagSuffix: String = "#>"

    override fun createTagSegment(content: String, location: Location): CommentTagSegment = CommentTagSegment(
        content = content,
        location = location,
    )
}

///

interface Template<T : Any>
{
    fun execute(model: T, writer: Writer)

    companion object
    {
        fun <T : Any> create(script: String, scriptEngineManager: ScriptEngineManager): Template<T> = ScriptTemplate<T>(
            script = script,
            scriptEngineManager = scriptEngineManager,
        )
    }

    class ScriptTemplate<T : Any>(
        val script: String,
        private val scriptEngineManager: ScriptEngineManager,
    ) : Template<T>
    {
        override fun execute(model: T, writer: Writer)
        {
            val scriptEngine = this.scriptEngineManager.getEngineByName("kotlin")
            scriptEngine.setBindings(ScriptContext.ENGINE_SCOPE, "model" to model, "writer" to writer)
            scriptEngine.eval(this.script)

            val scriptTemplateContract = scriptEngine.getInterface<ScriptTemplateContract>()
            scriptTemplateContract.execute()
        }
    }

    private interface ScriptTemplateContract
    {
        fun execute()
    }
}

fun <T> ScriptEngine.getInterface(clazz: Class<T>) = (this as Invocable).getInterface(clazz)

inline fun <reified T> ScriptEngine.getInterface() = this.getInterface(T::class.java)

fun ScriptEngine.setBindings(scope: Int, vararg values: Pair<String, Any?>)
{
    val bindings = this.createBindings()
    val map = values.toMap()
    bindings.putAll(map)
    this.setBindings(bindings, scope)
}

///

data class MyModel(
    val name: String,
)

fun generateScript(parsedSegments: ParsedSegments): String
{
    val buffer = StringBuilder()
    buffer.appendLine("fun execute()")
    buffer.appendLine("{")
    for (segment in parsedSegments)
    {
        val coordinates = "${segment.location.source.name}:${segment.location.startLineNumber}:${segment.location.startColumnNumber}"
        when (segment)
        {
            is RawSegment -> buffer.appendLine("append(\"${segment.content.kotlinEscape()}\") /// $coordinates")
            is BlockTagSegment -> buffer.appendLine("${segment.trimmedContent} /// $coordinates")
            is PrintTagSegment -> buffer.appendLine("append(${segment.trimmedContent}) /// $coordinates")
            else ->
            {
                buffer.appendLine("/**")
                for (line in segment.content.lines())
                {
                    buffer.appendLine(" * $line")
                }
                buffer.appendLine(" */")
            }
        }
    }
    buffer.appendLine("}")
    buffer.appendLine("fun append(value: Any?)")
    buffer.appendLine("{")
    buffer.appendLine("this.writer.append(\"\${value}\")")
    buffer.appendLine("}")
    return buffer.toString()
}

/*
fun main()
{
    val source = Source.fromFile(
        file = File("./examples/example1.kt"),
    )
    val segmentParser = SegmentParser()
    val parsedSegments = segmentParser.parseSegments(source)
    val script = generateScript(parsedSegments)
    val writer = System.out.writer()

    val template = Template.create<MyModel>(
        script = script,
        scriptEngineManager = ScriptEngineManager(),
    )

    if (template is Template.ScriptTemplate<*>)
    {
        println(template.script)
    }

    template.execute(
        model = MyModel(
            name = "Frank",
        ),
        writer = writer,
    )

    writer.flush()
}
*/

///

interface Widget<T>
{
    val value: T
}

abstract class WidgetBase

@Language("kotlin")
fun getScript(className: String) = """
class ${className} : com.luggsoft.k4.v3.core.Widget<Int>, com.luggsoft.k4.v3.core.WidgetBase()
{
    override val value: Int = 0
    
    fun execute()
    {
        TODO()
    }
}

${className}::class
"""

fun main()
{
    val className = ""
    val scriptEngine = ScriptEngineManager().getEngineByName("kotlin")
    val kClass = scriptEngine.eval(getScript("Noodles")) as KClass<*>
    val instance = kClass.createInstance()
    println(instance)
    println(instance is WidgetBase)
}
