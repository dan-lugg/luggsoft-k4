package com.luggsoft.k4.core

import com.luggsoft.k4.core.segments.CodeTagSegment
import com.luggsoft.k4.core.segments.EchoTagSegment
import com.luggsoft.k4.core.segments.MetaTagSegment
import com.luggsoft.k4.core.segments.RawSegment
import com.luggsoft.k4.core.segments.Segment
import com.luggsoft.k4.core.sources.Source
import com.luggsoft.k4.core.peekEquals
import com.luggsoft.k4.core.skip
import com.luggsoft.k4.core.tryConsumeNextDelimited

class DefaultSegmentParser : SegmentParser
{
    override fun parseSegments(source: Source): Iterable<Segment> = this.buildSegmentSequence(source).asIterable()

    private fun buildSegmentSequence(source: Source): Sequence<Segment> = sequence {
        // Initialize state, buffers, and create a source iterator
        var state = State.TEXT
        var segmentStartIndex = 0
        val contentBuilder = StringBuilder()
        val sourceIterator = source.createSourceIterator()

        // While the source iterator has more characters
        while (sourceIterator.hasNext())
        {
            when (state)
            {
                // If we're parsing in TEXT state
                State.TEXT ->
                {
                    when
                    {
                        // When we encounter a tag start
                        sourceIterator.peekEquals("<#") ->
                        {
                            // If there's buffered content
                            if (contentBuilder.isNotEmpty())
                            {
                                // Emit a raw segment of the buffered content and clear the buffer
                                val segment = RawSegment(
                                    content = contentBuilder.toString(),
                                    location = DefaultLocation(
                                        source = source,
                                        startIndex = segmentStartIndex,
                                        untilIndex = sourceIterator.index,
                                    ),
                                )
                                this.yield(segment)
                                contentBuilder.clear()
                            }

                            when
                            {
                                // If we've encountered a META tag
                                sourceIterator.peekEquals("<#@") ->
                                {
                                    // Skip the prefix and switch to META
                                    segmentStartIndex = sourceIterator.skip(3)
                                    state = State.META
                                }

                                // If we've encountered an ECHO tag
                                sourceIterator.peekEquals("<#=") ->
                                {
                                    // Skip the prefix and switch to ECHO
                                    segmentStartIndex = sourceIterator.skip(3)
                                    state = State.ECHO
                                }

                                // If we've encountered a CODE tag
                                sourceIterator.peekEquals("<#") ->
                                {
                                    // Skip the prefix and switch to CODE
                                    segmentStartIndex = sourceIterator.skip(2)
                                    state = State.CODE
                                }
                            }
                        }
                    }
                }

                // If we're parsing in META, ECHO, or CODE states
                State.META,
                State.ECHO,
                State.CODE
                ->
                {
                    // Try read-skipping a Kotlin raw string
                    sourceIterator.tryConsumeNextDelimited("\"\"\"", contentBuilder)

                    // Try read-skipping a Kotlin string
                    sourceIterator.tryConsumeNextDelimited("\"", contentBuilder)

                    // When we encounter an end tag
                    if (sourceIterator.peekEquals("#>"))
                    {
                        val segment = when (state)
                        {
                            // If we're parsing META create a meta tag segment with the buffered content
                            State.META -> MetaTagSegment(
                                content = contentBuilder.toString(),
                                location = DefaultLocation(
                                    source = source,
                                    startIndex = segmentStartIndex,
                                    untilIndex = sourceIterator.index,
                                ),
                            )

                            // If we're parsing ECHO create an echo tag segment with the buffered content
                            State.ECHO -> EchoTagSegment(
                                content = contentBuilder.toString(),
                                location = DefaultLocation(
                                    source = source,
                                    startIndex = segmentStartIndex,
                                    untilIndex = sourceIterator.index,
                                ),
                            )

                            // If we're parsing CODE create a code tag segment with the buffered content
                            State.CODE -> CodeTagSegment(
                                content = contentBuilder.toString(),
                                location = DefaultLocation(
                                    source = source,
                                    startIndex = segmentStartIndex,
                                    untilIndex = sourceIterator.index,
                                ),
                            )

                            // If the parsing state is anything else, puke
                            else -> throw SegmentParserException(
                                message = "Invalid parsing state ($state) at index (${sourceIterator.index})"
                            )
                        }

                        // Emit the tag segment, skip the suffix, clear the buffer, and switch to TEXT
                        this.yield(segment)
                        segmentStartIndex = sourceIterator.skip(2)
                        contentBuilder.clear()
                        state = State.TEXT
                        continue
                    }
                }
            }

            // If source iterator still has more characters
            if (sourceIterator.hasNext())
            {
                // Append them to the buffer
                contentBuilder.append(sourceIterator.next())
            }
            else
            {
                // Otherwise, bail out of the parsing loop
                break
            }
        }

        // If the buffer still has remaining content
        if (contentBuilder.isNotEmpty())
        {
            // Create and emit a raw segment of the remaining content
            val segment = RawSegment(
                content = contentBuilder.toString(),
                location = DefaultLocation(
                    source = source,
                    startIndex = segmentStartIndex,
                    untilIndex = sourceIterator.index,
                ),
            )
            this.yield(segment)
        }
    }

    private enum class State
    {
        TEXT,
        META,
        ECHO,
        CODE,
    }

}
