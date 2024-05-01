package com.luggsoft.k4.core

import com.facebook.ktfmt.format.Formatter
import com.facebook.ktfmt.format.FormattingOptions
import com.luggsoft.common.text.kotlinEscape
import com.luggsoft.k4.core.segments.CodeTagSegment
import com.luggsoft.k4.core.segments.EchoTagSegment
import com.luggsoft.k4.core.segments.MetaTagSegment
import com.luggsoft.k4.core.segments.RawSegment
import com.luggsoft.k4.core.segments.Segment
import com.luggsoft.k4.core.segments.TagSegmentBase
import java.io.Reader
import java.io.StringWriter
import java.io.Writer
import javax.script.Invocable
import javax.script.ScriptEngineManager
import kotlin.reflect.KClass

class DefaultTemplateWriter : TemplateWriter
{
    override fun <TModel : Any> writeTemplate(writer: Writer, segments: Iterable<Segment>)
    {
        StringWriter().use { innerWriter ->
            this.writeTemplateHeader(innerWriter, TODO())
            this.writeTemplateScript(innerWriter, segments)
            this.writeTemplateFooter(innerWriter)
            innerWriter.flush()
            val template = innerWriter.toString()
            val formattingOptions = FormattingOptions(
                style = FormattingOptions.Style.FACEBOOK,
                blockIndent = 4,
                continuationIndent = 4,
                removeUnusedImports = true,
            )
            val formattedTemplate = Formatter.format(formattingOptions, template)
            writer.write(formattedTemplate)
        }
        writer.flush()
    }

    private fun writeTemplateHeader(writer: Writer, modelKClass: KClass<*>)
    {
        writer.appendLine("import java.io.Writer")
        writer.appendLine("import ${modelKClass.qualifiedName}")
        writer.appendLine("")
        writer.appendLine("fun execute(writer: Writer, model: ${modelKClass.simpleName})")
        writer.appendLine("{")
    }

    private fun writeTemplateScript(writer: Writer, segments: Iterable<Segment>)
    {
        for (segment in segments)
        {
            when (segment)
            {
                is RawSegment ->
                {
                    // this.writeLocationInfo(writer, segment.location)
                    writer.appendLine("writer.append(\"${segment.content.kotlinEscape()}\")")
                }

                is TagSegmentBase ->
                {
                    when (segment)
                    {
                        is MetaTagSegment ->
                        {
                            // this.writeLocationInfo(writer, segment.location)
                            writer.appendLine("meta(\"${segment.content.kotlinEscape()}\")")
                        }

                        is EchoTagSegment ->
                        {
                            // this.writeLocationInfo(writer, segment.location)
                            writer.appendLine("writer.append(${segment.content.trim()})")
                        }

                        is CodeTagSegment ->
                        {
                            // this.writeLocationInfo(writer, segment.location)
                            for (line in segment.content.lines())
                            {
                                writer.appendLine(line.trim())
                            }
                        }
                    }
                }
            }
        }
    }

    private fun writeTemplateFooter(writer: Writer)
    {
        writer.appendLine("}")
        writer.appendLine("")
    }

    private fun writeLocationInfo(writer: Writer, location: Location)
    {
        writer.appendLine("@LocationInfo(sourceName = \"${location.source.name}\", startIndex = ${location.startIndex}, untilIndex = ${location.untilIndex})")
    }
}

///

interface Template<TModel : Any>
{
    fun render(writer: Writer, model: TModel? = null)
}

internal const val KOTLIN_ENGINE_NAME = "kotlin"

class ScriptTemplate<TModel : Any>(
    private val scriptReader: Reader,
    private val scriptEngineManager: ScriptEngineManager,
) : Template<TModel>
{
    @Suppress("UNCHECKED_CAST")
    override fun render(writer: Writer, model: TModel?)
    {
        val scriptEngine = this.scriptEngineManager.getEngineByName(KOTLIN_ENGINE_NAME)
        scriptEngine.eval(this.scriptReader)

        if (scriptEngine is Invocable)
        {
            val invocable = scriptEngine as Invocable
            val innerTemplate = invocable.getInterface(Template::class.java) as Template<Any>
            innerTemplate.render(writer, model)
        }

        writer.flush()
    }
}

///

data class Coordinate(
    val x: Float,
    val y: Float,
    val z: Float,
)

const val TEMPLATE_CONTENT = """
The coordinates are x = <#= model.x #>, y = <#= model.y #>, and z = <#= model.z #>.
<# if (true) { #>
This statement is true!
<# } #>
Thank you!
"""

fun main()
{
    /*
    val source = StringSource(
        name = "example",
        content = TEMPLATE_CONTENT,
    )
    val segmentParser = DefaultSegmentParser(
        logger = LoggerFactory.getLogger("parser"),
    )
    val segments = segmentParser.parseSegments(
        source = source,
    )
    val templateWriter = DefaultTemplateWriter()
    val writer = StringWriter()
    templateWriter.writeTemplate(
        writer = writer,
        segments = segments,
    )
    writer.flush()
    val templateScript = writer.toString()

    println(templateScript)

    val template = ScriptTemplate<Coordinate>(
        scriptReader = StringReader(templateScript),
        scriptEngineManager = ScriptEngineManager(),
    )
    val outputWriter = System.out.writer()
    template.render(
        writer = outputWriter,
        model = Coordinate(
            x = 1.2f,
            y = 2.3f,
            z = 3.4f,
        )
    )
    outputWriter.flush()
    */
}
