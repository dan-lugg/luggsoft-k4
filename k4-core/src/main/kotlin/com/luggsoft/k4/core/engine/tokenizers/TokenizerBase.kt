package com.luggsoft.k4.core.engine.tokenizers

import com.luggsoft.common.logger
import com.luggsoft.k4.core.Source
import com.luggsoft.k4.core.engine.tokenizers.tokens.NoteToken
import com.luggsoft.k4.core.engine.tokenizers.tokens.TextToken
import com.luggsoft.k4.core.engine.tokenizers.tokens.TextTokenizerState
import com.luggsoft.k4.core.engine.tokenizers.tokens.Token
import com.luggsoft.k4.core.engine.tokenizers.tokens.TokenizerState

abstract class TokenizerBase(
    private val tokenizerSettings: TokenizerSettings
) : Tokenizer
{
    private var tokenizerState: TokenizerState = TextTokenizerState(this.tokenizerSettings)

    final override fun tokenizeSource(source: Source, startIndex: Int): List<Token>
    {
        try
        {
            var index = startIndex
            val tokens = mutableListOf<Token>()
            tokenizing@ while (index < source.text.length)
            {
                val token = this.tokenizerState.getNextToken(source, index, this::setTokenProviderState)
                index = token.location.untilIndex
                when (token)
                {
                    is TextToken ->
                    {
                        if (tokens.isEmpty())
                        {
                            if (token.text.isBlank() && this.tokenizerSettings.discardFirstBlankTextToken)
                            {
                                continue@tokenizing
                            }
                        }
                        if (token.text.isEmpty() && this.tokenizerSettings.discardEmptyTextTokens)
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
                }
                tokens.add(token)
            }
            return tokens
        }
        catch (exception: Exception)
        {
            ("Failed to tokenize source").also { message ->
                this.logger.error(message, exception)
                throw TokenizerException(message, exception)
            }
        }
    }

    private fun setTokenProviderState(tokenizerState: TokenizerState)
    {
        this.tokenizerState = tokenizerState
    }
}
