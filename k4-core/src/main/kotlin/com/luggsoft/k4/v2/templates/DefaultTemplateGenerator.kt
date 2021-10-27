package com.luggsoft.k4.v2.templates

import com.luggsoft.k4.v2.sources.segments.CommentTagSegment
import com.luggsoft.k4.v2.sources.segments.KotlinTagSegment
import com.luggsoft.k4.v2.sources.segments.PrintTagSegment
import com.luggsoft.k4.v2.sources.segments.RawSegment
import com.luggsoft.k4.v2.sources.segments.Segment
import com.luggsoft.k4.v2.sources.segments.SegmentList
import javax.script.ScriptEngineManager

class DefaultTemplateGenerator(
    private val metaSegmentParser: MetaSegmentParser,
    private val scriptEngineManager: ScriptEngineManager,
    private val templateBuilderSettings: TemplateBuilderSettings,
) : TemplateGenerator
{

    override fun generateTemplate(segmentList: SegmentList): Template
    {
        val buffer = StringBuilder()

        val meta = this.metaSegmentParser.parseMetaSegments(segmentList.metaTagSegments)

        buffer.appendLine("import ${meta.modelKClass.qualifiedName}")
        buffer.appendLine("import ${meta.templateWriterKClass.qualifiedName}")
        buffer.appendLine("import ${meta.templateLoggerKClass.qualifiedName}")

        for (importType in meta.importTypeNames.orEmpty())
        {
            buffer.appendLine("import $importType")
        }

        buffer.appendLine("fun render(")
        buffer.appendLine("${meta.modelParamName}: ${meta.modelKClass.simpleName},")
        buffer.appendLine("templateWriter: ${meta.templateWriterKClass.simpleName},")
        buffer.appendLine("templateLogger: ${meta.templateLoggerKClass.simpleName},")
        buffer.appendLine(")")
        buffer.appendLine("{")

        for (segment in segmentList.sortedBy(Segment::location))
        {
            when (segment)
            {
                is RawSegment -> buffer.appendLine("templateWriter.${meta.templateWriterKCallable.name}(${segment.content.kotlinEscape()})")
                is PrintTagSegment -> buffer.appendLine("templateWriter.${meta.templateWriterKCallable.name}(\"\${${segment.content.trim()}}\")")
                is KotlinTagSegment -> buffer.appendLine(segment.content.trim())

                is CommentTagSegment ->
                {
                    if (this.templateBuilderSettings[TemplateBuilderFlags.INCLUDE_COMMENTS_INLINE])
                    {
                        for (line in segment.content.trim().lines())
                        {
                            buffer.appendLine("// $line")
                        }
                    }

                    if (this.templateBuilderSettings[TemplateBuilderFlags.INCLUDE_COMMENTS_LOGGED])
                    {
                        for (line in segment.content.trim().lines())
                        {
                            buffer.appendLine("templateLogger.${meta.templateLoggerKCallable.name}(${line.kotlinEscape()})")
                        }
                    }
                }
            }
        }

        buffer.appendLine("}")

        return DefaultTemplate(
            script = buffer.toString(),
            source = segmentList.source,
            scriptEngineManager = this.scriptEngineManager,
        )
    }
}
