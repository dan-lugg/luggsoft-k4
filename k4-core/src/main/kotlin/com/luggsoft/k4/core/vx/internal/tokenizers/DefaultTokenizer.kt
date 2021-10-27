package com.luggsoft.k4.core.vx.internal.tokenizers

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.luggsoft.k4.core.vx.Location
import com.luggsoft.k4.core.vx.io.Source
import com.luggsoft.k4.core.vx.util.isIntersect

class DefaultTokenizer(
    private val tokenReaders: List<TokenReader>,
    private val tokenizerFlags: TokenizerFlags,
) : Tokenizer
{
    private val tokenComparator = compareBy<Token>(
        { token -> token.location.startIndex },
        { token -> token.location.untilIndex },
    )

    constructor(vararg tokenReaders: TokenReader, tokenizerFlags: TokenizerFlags) : this(
        tokenReaders = tokenReaders.toList(),
        tokenizerFlags = tokenizerFlags,
    )

    override fun tokenize(source: Source, startIndex: Int): List<Token>
    {
        var index = startIndex
        val buffer = StringBuilder()
        val tokens = mutableListOf<Token>()

        while (index < source.content.length)
        {
            var token: Token? = null

            for (tokenReader in tokenReaders)
            {
                token = tokenReader.readToken(
                    source = source,
                    startIndex = index,
                )

                when (token)
                {
                    null -> continue

                    else ->
                    {
                        if (buffer.isNotEmpty())
                        {
                            val rawToken = RawToken(
                                value = buffer,
                                location = Location(
                                    source = source,
                                    startIndex = index - buffer.length,
                                    untilIndex = index - 1,
                                ),
                            )
                            tokens.add(rawToken)
                        }

                        tokens.add(token)
                        index = token.location.untilIndex + 1
                        buffer.clear()
                        break
                    }
                }
            }

            if (token == null)
            {
                buffer.append(source.content[index++])
            }
        }

        if (buffer.isNotEmpty())
        {
            val rawToken = RawToken(
                value = buffer,
                location = Location(
                    source = source,
                    startIndex = index - buffer.length,
                    untilIndex = index - 1,
                ),
            )
            tokens.add(rawToken)
        }

        return tokens.sortedWith(this.tokenComparator)
            .also(this::validateTokens)
    }

    object Instance : Tokenizer by DefaultTokenizer(
        tokenReaders = listOf(
            MetaTokenReader(),
            PrintTokenReader(),
            KotlinTokenReader(),
            CommentTokenReader(),
        ),
        tokenizerFlags = TokenizerFlags.createDefault(),
    )

    private fun validateTokens(tokens: List<Token>)
    {
        val objectMapper = jacksonObjectMapper()

        for ((token1, token2) in tokens.zipWithNext())
        {
            if (token1.location.range.isIntersect(token2.location.range))
            {
                println("WARNING: Range intersect between:")
                println("    > ${objectMapper.writeValueAsString(token1)}")
                println("    > ${objectMapper.writeValueAsString(token2)}")
                println()
            }
        }
    }
}
