package com.luggsoft.k4.core

import com.facebook.ktfmt.format.Formatter
import com.facebook.ktfmt.format.FormattingOptions
import com.fasterxml.jackson.databind.ObjectMapper
import com.luggsoft.k4.core.segments.CodeTagSegment
import com.luggsoft.k4.core.segments.EchoTagSegment
import com.luggsoft.k4.core.segments.MetaTagSegment
import com.luggsoft.k4.core.segments.RawSegment
import com.luggsoft.k4.core.segments.Segment
import com.luggsoft.k4.core.segments.TagSegmentBase
import java.io.StringWriter
import java.io.Writer

class DefaultTemplateWriter(
    private val objectMapper: ObjectMapper,
) : TemplateWriter
{
    override fun writeTemplate(writer: Writer, segments: Iterable<Segment>)
    {
        StringWriter().use { innerWriter ->
            this.writeTemplateHeader(innerWriter)
            this.writeTemplateScript(innerWriter, segments)
            this.writeTemplateFooter(innerWriter)
            innerWriter.flush()
            val template = innerWriter.toString()
            val formattingOptions = FormattingOptions(
                style = FormattingOptions.Style.GOOGLE,
                blockIndent = 4,
                continuationIndent = 4,
                removeUnusedImports = true,
            )
            val formattedTemplate = Formatter.format(formattingOptions, template)
            writer.write(formattedTemplate)
        }
        writer.flush()
    }

    private fun writeTemplateHeader(writer: Writer)
    {
        writer.appendLine("import java.io.Writer")
        writer.appendLine("")
        writer.appendLine("fun execute(writer: Writer)")
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
                    writer.appendLine("writer.append(${this.objectMapper.writeValueAsString(segment.content)})")
                }
                is TagSegmentBase ->
                {
                    when (segment)
                    {
                        is MetaTagSegment ->
                        {
                            writer.appendLine("meta(${this.objectMapper.writeValueAsString(segment.content)})")
                        }
                        is EchoTagSegment ->
                        {
                            writer.appendLine("writer.append(${segment.content.trim()})")
                        }
                        is CodeTagSegment ->
                        {
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
    }
}
