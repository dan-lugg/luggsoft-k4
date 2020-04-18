package com.luggsoft.k4.core.engine.tokenizers

import com.luggsoft.common.logger
import com.luggsoft.k4.core.Source
import com.luggsoft.k4.core.engine.tokenizers.tokens.BodyToken
import com.luggsoft.k4.core.engine.tokenizers.tokens.InfoToken
import com.luggsoft.k4.core.engine.tokenizers.tokens.NoteToken
import com.luggsoft.k4.core.engine.tokenizers.tokens.TextToken
import com.luggsoft.k4.core.engine.tokenizers.tokens.TextTokenizerState
import com.luggsoft.k4.core.engine.tokenizers.tokens.Token
import com.luggsoft.k4.core.engine.tokenizers.tokens.TokenizerState

class DefaultTokenizer(
    private val tokenizerSettings: TokenizerSettings
) : Tokenizer
{
    /**
     *
     */
    private var tokenizerState: TokenizerState = TextTokenizerState(this.tokenizerSettings)

    /**
     * TODO
     *
     * @param source
     * @param startIndex
     * @return
     */
    override fun tokenizeSource(source: Source, startIndex: Int): List<Token>
    {
        // initialize
        var index = startIndex
        val tokens = mutableListOf<Token>()
        try
        {
            // while not EOF
            tokenizing@ while (index < source.text.length)
            {
                // get token and set index
                val token = this.tokenizerState.getNextToken(source, index, this::setTokenProviderState)
                index = token.location.untilIndex

                // determine if token should be skipped
                when (token)
                {
                    is TextToken ->
                    {
                        if (tokens.isEmpty() && token.text.isBlank())
                        {
                            continue@tokenizing
                        }
                        if (token.text.isEmpty())
                        {
                            continue@tokenizing
                        }
                    }
                    is NoteToken ->
                    {
                        if (this.tokenizerSettings.discardNoteTokens)
                        {
                            continue@tokenizing
                        }
                    }
                    is InfoToken ->
                    {
                        if (tokens.isNotEmpty())
                        {
                            TODO("Not valid at the point")
                        }
                    }
                }

                // add token
                tokens.add(token)
            }

            // return tokens
            return tokens
                .sortedBy { token -> if (token is InfoToken) 1 else 0 }
                .sortedBy { token -> if (token is BodyToken) 1 else 0 }
        }
        catch (exception: Exception)
        {
            ("Failed to tokenize source").also { message ->
                this.logger.error(message, exception)
                throw TokenizerException(message, exception)
            }
        }
    }

    /**
     * TODO
     *
     * @param tokenizerState
     */
    private fun setTokenProviderState(tokenizerState: TokenizerState)
    {
        this.tokenizerState = tokenizerState
    }

    object Instance : Tokenizer by DefaultTokenizer(
        tokenizerSettings = TokenizerSettings.Default
    )
}
