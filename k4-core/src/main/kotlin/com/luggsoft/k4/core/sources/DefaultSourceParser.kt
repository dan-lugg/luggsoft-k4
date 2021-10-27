package com.luggsoft.k4.core.sources

import com.luggsoft.k4.core.sources.segments.CommentTagSegment
import com.luggsoft.k4.core.sources.segments.KotlinTagSegment
import com.luggsoft.k4.core.sources.segments.Location
import com.luggsoft.k4.core.sources.segments.MetaTagSegment
import com.luggsoft.k4.core.sources.segments.PrintTagSegment
import com.luggsoft.k4.core.sources.segments.RawSegment
import com.luggsoft.k4.core.sources.segments.Segment
import com.luggsoft.k4.core.sources.segments.SegmentList
import com.luggsoft.k4.core.sources.segments.TagSegmentBase

class DefaultSourceParser(
    private val tagSegmentReaders: List<TagSegmentReaderBase>,
    private val sourceParserSettings: SourceParserSettings,
) : SourceParser
{
    override fun parseSource(source: Source): SegmentList
    {
        var index = 0
        val buffer = StringBuilder()
        val segments = mutableListOf<Segment>()

        while (index < source.content.length)
        {
            var tagSegment: TagSegmentBase? = null

            for (tagSegmentReader in this.tagSegmentReaders)
            {
                tagSegment = tagSegmentReader.readTagSegment(source, index)
                    ?: continue

                if (buffer.isNotEmpty())
                {
                    val rawSegment = RawSegment(
                        content = buffer.toString(),
                        location = Location(
                            startIndex = index - buffer.length,
                            untilIndex = index - 1,
                        )
                    )
                    segments.add(rawSegment)
                }

                index = tagSegment.location.untilIndex + 1
                segments.add(tagSegment)
                buffer.clear()

                if (this.sourceParserSettings[SourceParserFlags.SKIP_TAG_TRAILING_NEWLINES])
                {
                    index = this.skipTrailingNewlines(
                        source = source,
                        startIndex = index,
                    )
                }

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
                    startIndex = index - buffer.length,
                    untilIndex = index - 1,
                )
            )
            segments.add(rawSegment)
        }

        return SegmentList(
            source = source,
            segments = segments,
        )
    }

    private fun skipTrailingNewlines(source: Source, startIndex: Int): Int
    {
        var index = startIndex

        do
        {
            when (source.content[index++])
            {
                '\r',
                '\n' -> continue
                else -> break
            }
        }
        while (index < source.content.length)

        return index - 1
    }

    abstract class TagSegmentReaderBase
    {
        abstract val tagPrefix: String

        abstract val tagSuffix: String

        abstract fun createTagSegment(content: String, location: Location): TagSegmentBase

        fun readTagSegment(source: Source, startIndex: Int): TagSegmentBase?
        {
            if (!source.content.substring(startIndex).startsWith(this.tagPrefix))
            {
                return null
            }

            var index = startIndex + this.tagPrefix.length
            val buffer = StringBuilder()

            while (index < source.content.length)
            {
                if (buffer.endsWith(this.tagSuffix))
                {
                    return this.createTagSegment(
                        content = buffer.dropLast(this.tagSuffix.length).toString(),
                        location = Location(
                            startIndex = startIndex,
                            untilIndex = index - 1,
                        )
                    )
                }

                buffer.append(source.content[index++])
            }

            return null
        }
    }

    class MetaTagSegmentReader : TagSegmentReaderBase()
    {
        override val tagPrefix: String = "<#@"

        override val tagSuffix: String = "#>"

        override fun createTagSegment(content: String, location: Location): TagSegmentBase = MetaTagSegment(
            content = content,
            location = location,
        )
    }

    class PrintTagSegmentReader : TagSegmentReaderBase()
    {
        override val tagPrefix: String = "<#="

        override val tagSuffix: String = "#>"

        override fun createTagSegment(content: String, location: Location): TagSegmentBase = PrintTagSegment(
            content = content,
            location = location,
        )
    }

    class KotlinTagSegmentReader : TagSegmentReaderBase()
    {
        override val tagPrefix: String = "<#!"

        override val tagSuffix: String = "#>"

        override fun createTagSegment(content: String, location: Location): TagSegmentBase = KotlinTagSegment(
            content = content,
            location = location,
        )
    }

    class CommentTagSegmentReader : TagSegmentReaderBase()
    {
        override val tagPrefix: String = "<#*"

        override val tagSuffix: String = "#>"

        override fun createTagSegment(content: String, location: Location): TagSegmentBase = CommentTagSegment(
            content = content,
            location = location,
        )
    }

    object Instance : SourceParser by DefaultSourceParser(
        tagSegmentReaders = listOf(
            MetaTagSegmentReader(),
            PrintTagSegmentReader(),
            KotlinTagSegmentReader(),
            CommentTagSegmentReader(),
        ),
        sourceParserSettings = SourceParserSettings.createDefault(),
    )
}
