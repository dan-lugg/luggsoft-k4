package com.luggsoft.k4.core.segments.parsers

import com.luggsoft.k4.core.DefaultLocation
import com.luggsoft.k4.core.segments.CodeTagSegment
import com.luggsoft.k4.core.segments.EchoTagSegment
import com.luggsoft.k4.core.segments.MetaTagSegment
import com.luggsoft.k4.core.segments.RawSegment
import com.luggsoft.k4.core.segments.Segment
import com.luggsoft.k4.core.sources.Source
import com.luggsoft.k4.core.sources.iterators.peekEquals
import com.luggsoft.k4.core.sources.iterators.skip
import org.slf4j.Logger

class DefaultSegmentParser(
    private val logger: Logger,
) : SegmentParser
{
    override fun parseSegments(source: Source): Iterable<Segment> = this.buildSegmentSequence(source).asIterable()

    private fun buildSegmentSequence(source: Source): Sequence<Segment> = sequence {
        // Initialize state, buffers, and create a source iterator
        var state = State.TEXT
        var segmentStartIndex = 0
        val contentBuilder = StringBuilder()
        val sourceIterator = source.createSourceIterator()
        val logger = this@DefaultSegmentParser.logger

        // Initialize line/column numbers
        var startLineNumber = 1
        var untilLineNumber = 1
        var startColumnNumber = 1
        var untilColumnNumber = 1

        // Start
        logger.debug("Segment parsing sequence state initialized")

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
                                // Create a parsed segment
                                val segment = RawSegment(
                                    content = contentBuilder.toString(),
                                    location = DefaultLocation(
                                        source = source,
                                        startIndex = segmentStartIndex,
                                        untilIndex = sourceIterator.index,
                                        startLineNumber = startLineNumber,
                                        untilLineNumber = untilLineNumber,
                                        startColumnNumber = startColumnNumber,
                                        untilColumnNumber = untilColumnNumber
                                    ),
                                )

                                // Log the segment
                                logger.debug("Parsed segment, $segment")

                                // Update the line/column numbers, emit a raw segment of the buffered content, and clear the buffer
                                startLineNumber = untilLineNumber
                                startColumnNumber = untilColumnNumber
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
                                    untilColumnNumber += 3
                                    state = State.META
                                }

                                // If we've encountered an ECHO tag
                                sourceIterator.peekEquals("<#=") ->
                                {
                                    // Skip the prefix and switch to ECHO
                                    segmentStartIndex = sourceIterator.skip(3)
                                    untilColumnNumber += 3
                                    state = State.ECHO
                                }

                                // If we've encountered a CODE tag
                                sourceIterator.peekEquals("<#") ->
                                {
                                    // Skip the prefix and switch to CODE
                                    segmentStartIndex = sourceIterator.skip(2)
                                    untilColumnNumber += 2
                                    state = State.CODE
                                }
                            }
                        }
                    }
                }

                // If we're parsing in META, ECHO, or CODE states
                State.META,
                State.ECHO,
                State.CODE ->
                {
                    // TODO: re-enable
                    // Try read-skipping a Kotlin raw string
                    // sourceIterator.tryConsumeNextDelimited("\"\"\"", contentBuilder)

                    // TODO: re-enable
                    // Try read-skipping a Kotlin string
                    // sourceIterator.tryConsumeNextDelimited("\"", contentBuilder)

                    // When we encounter an end tag
                    if (sourceIterator.peekEquals("#>"))
                    {
                        // Create a location for the tag, and update the line/column numbers
                        val location = DefaultLocation(
                            source = source,
                            startIndex = segmentStartIndex,
                            untilIndex = sourceIterator.index,
                            startLineNumber = startLineNumber,
                            untilLineNumber = untilLineNumber,
                            startColumnNumber = startColumnNumber,
                            untilColumnNumber = untilColumnNumber,
                        )
                        startLineNumber = untilLineNumber
                        startColumnNumber = untilColumnNumber
                        segmentStartIndex += 2

                        // Create a parsed segment
                        val segment = when (state)
                        {
                            // If we're parsing META create a meta tag segment with the buffered content
                            State.META -> MetaTagSegment(
                                content = contentBuilder.toString(),
                                location = location,
                            )

                            // If we're parsing ECHO create an echo tag segment with the buffered content
                            State.ECHO -> EchoTagSegment(
                                content = contentBuilder.toString(),
                                location = location,
                            )

                            // If we're parsing CODE create a code tag segment with the buffered content
                            State.CODE -> CodeTagSegment(
                                content = contentBuilder.toString(),
                                location = location,
                            )

                            // If the parsing state is anything else, puke
                            else ->
                            {
                                val message = "Invalid parsing state ($state) at index (${sourceIterator.index})"
                                logger.error(message)
                                throw SegmentParserException(message)
                            }
                        }

                        // Log the segment
                        logger.debug("Parsed segment, $segment")

                        // Emit the tag segment, skip the suffix, clear the buffer, and switch to TEXT
                        this.yield(segment)
                        segmentStartIndex = sourceIterator.skip(2)
                        untilColumnNumber += 2
                        contentBuilder.clear()
                        state = State.TEXT
                        continue
                    }
                }
            }

            // If source iterator still has more characters
            if (sourceIterator.hasNext())
            {
                // Check for newlines
                when
                {
                    // On Windows newline
                    sourceIterator.peekEquals("\r\n") ->
                    {
                        // Update line/column numbers and append to the buffer
                        untilLineNumber += 1
                        untilColumnNumber = 1
                        contentBuilder.append(sourceIterator.next(2))
                    }

                    // On *nix newline
                    sourceIterator.peekEquals("\n") ->
                    {
                        // Update line/column numbers and append to the buffer
                        untilLineNumber += 1
                        untilColumnNumber = 1
                        contentBuilder.append(sourceIterator.next(1))
                    }

                    // Otherwise
                    else ->
                    {
                        // Increment the column number and append to the buffer
                        untilColumnNumber += 1
                        contentBuilder.append(sourceIterator.next())
                    }
                }
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
            // Create a raw segment of the remaining content
            val segment = RawSegment(
                content = contentBuilder.toString(),
                location = DefaultLocation(
                    source = source,
                    startIndex = segmentStartIndex,
                    untilIndex = sourceIterator.index,
                    startLineNumber = startLineNumber,
                    untilLineNumber = untilLineNumber,
                    startColumnNumber = startColumnNumber,
                    untilColumnNumber = untilColumnNumber,
                ),
            )

            // Log the segment
            logger.debug("Parsed segment, $segment")

            // Emit the segment
            this.yield(segment)
        }

        // End
        logger.debug("Segment parsing complete")
    }

    private enum class State
    {
        TEXT,
        META,
        ECHO,
        CODE,
    }
}
