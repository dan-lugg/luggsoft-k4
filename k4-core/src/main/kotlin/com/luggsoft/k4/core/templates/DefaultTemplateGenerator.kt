package com.luggsoft.k4.core.templates

import com.luggsoft.k4.core.sources.DefaultSourceParser
import com.luggsoft.k4.core.sources.Source
import com.luggsoft.k4.core.sources.SourceParser
import com.luggsoft.k4.core.sources.segments.CommentTagSegment
import com.luggsoft.k4.core.sources.segments.IncludesTagSegment
import com.luggsoft.k4.core.sources.segments.KotlinTagSegment
import com.luggsoft.k4.core.sources.segments.PrintTagSegment
import com.luggsoft.k4.core.sources.segments.RawSegment
import com.luggsoft.k4.core.sources.segments.Segment
import com.luggsoft.k4.core.sources.segments.SegmentList
import org.slf4j.Logger
import java.io.File
import java.io.Writer
import javax.script.ScriptEngineManager

class DefaultTemplateGenerator(
    private val sourceParser: SourceParser,
    private val metaSegmentParser: MetaSegmentParser,
    private val scriptEngineManager: ScriptEngineManager,
    private val templateGeneratorSettings: TemplateGeneratorSettings,
) : TemplateGenerator
{
    override fun generateTemplate(segmentList: SegmentList): Template<Any>
    {
        val scriptBuilder = StringBuilder()
        val meta = this.metaSegmentParser.parseMetaSegments(segmentList.metaTagSegments)

        scriptBuilder.appendLine("import ${meta.modelKClass.qualifiedName}")
        scriptBuilder.appendLine("import ${Writer::class.qualifiedName}")
        scriptBuilder.appendLine("import ${Logger::class.qualifiedName}")

        for (importType in meta.importTypeNames)
        {
            scriptBuilder.appendLine("import $importType")
        }

        scriptBuilder.appendLine("fun render(")
        scriptBuilder.appendLine("${meta.modelParamName}: ${meta.modelKClass.simpleName},")
        scriptBuilder.appendLine("writer: ${Writer::class.simpleName},")
        scriptBuilder.appendLine("logger: ${Logger::class.simpleName},")
        scriptBuilder.appendLine(")")
        scriptBuilder.appendLine("{")

        for (segment in segmentList.sortedBy(Segment::location))
        {
            when (segment)
            {
                is RawSegment -> scriptBuilder.appendLine("writer.append(\"${segment.content.kotlinEscape()}\")")
                is PrintTagSegment -> scriptBuilder.appendLine("writer.append(\"\${${segment.content.trim()}}\")")
                is KotlinTagSegment -> scriptBuilder.appendLine(segment.content.trim())

                is CommentTagSegment ->
                {
                    if (this.templateGeneratorSettings[TemplateGeneratorFlags.INCLUDE_COMMENTS_INLINE])
                    {
                        for (line in segment.content.trim().lines())
                        {
                            scriptBuilder.appendLine("// $line")
                        }
                    }

                    if (this.templateGeneratorSettings[TemplateGeneratorFlags.INCLUDE_COMMENTS_LOGGED])
                    {
                        for (line in segment.content.trim().lines())
                        {
                            scriptBuilder.appendLine("logger.info(\"${line.kotlinEscape()}\")")
                        }
                    }
                }

                is IncludesTagSegment ->
                {
                    val fileName = segment.content.trim()
                    val source = Source.fromFile(
                        file = File(fileName),
                        charset = Charsets.UTF_8,
                    )
                    val includesSegmentList = this.sourceParser.parseSource(source)
                    TODO("Do something meaningful with $includesSegmentList")
                }
            }
        }

        scriptBuilder.appendLine("}")

        val template = DefaultTemplate(
            source = segmentList.source,
            script = scriptBuilder.toString(),
            modelKClass = meta.modelKClass,
            scriptEngineManager = this.scriptEngineManager,
        )

        @Suppress("UNCHECKED_CAST")
        return template as Template<Any>
    }

    object Instance : TemplateGenerator by DefaultTemplateGenerator(
        sourceParser = DefaultSourceParser.Instance,
        metaSegmentParser = DefaultMetaSegmentParser.Instance,
        scriptEngineManager = ScriptEngineManager(),
        templateGeneratorSettings = TemplateGeneratorSettings.createDefault(),
    )
}
