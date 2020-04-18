package com.luggsoft.k4.core.engine.tokenizers.tokens

import com.luggsoft.k4.core.Source
import com.luggsoft.k4.core.engine.Location
import com.luggsoft.k4.core.engine.tokenizers.TokenizerSettings

class TextTokenizerState(
    tokenizerSettings: TokenizerSettings
) : TokenizerStateBase(
    tokenizerSettings = tokenizerSettings
)
{
    @Throws(TokenizerStateException::class)
    override fun getNextToken(source: Source, startIndex: Int, tokenizerStateSetter: TokenizerStateSetter): Token
    {
        var index = startIndex
        val stringBuffer = StringBuffer()
        while (index < source.text.length)
        {
            stringBuffer.append(source.text[index++])
            if (stringBuffer.endsWith("<#"))
            {
                val tokenProviderState = when (source.text[index++])
                {
                    '!' -> CodeTokenizerState(this.tokenizerSettings)
                    '=' -> EchoTokenizerState(this.tokenizerSettings)
                    '+' -> BodyTokenizerState(this.tokenizerSettings)
                    '@' -> InfoTokenizerState(this.tokenizerSettings)
                    '*' -> NoteTokenizerState(this.tokenizerSettings)
                    else -> UnknownTokenizerState
                }
                tokenizerStateSetter(tokenProviderState)
                return TextToken(
                    text = stringBuffer.removeSuffix("<#").toString(),
                    location = Location(
                        name = source.name,
                        startIndex = startIndex,
                        untilIndex = index
                    )
                )
            }
        }
        return TextToken(
            text = stringBuffer.toString(),
            location = Location(
                name = source.name,
                startIndex = startIndex,
                untilIndex = index
            )
        )
    }
}
