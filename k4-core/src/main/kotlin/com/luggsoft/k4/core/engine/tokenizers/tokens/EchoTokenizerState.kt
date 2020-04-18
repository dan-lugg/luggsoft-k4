package com.luggsoft.k4.core.engine.tokenizers.tokens

import com.luggsoft.k4.core.Source
import com.luggsoft.k4.core.engine.Location
import com.luggsoft.k4.core.engine.tokenizers.TokenizerSettings

class EchoTokenizerState(
    tokenizerSettings: TokenizerSettings
) : TokenizerStateBase(
    tokenizerSettings = tokenizerSettings
)
{
    @Throws(TokenizerStateException::class)
    override fun getNextToken(source: Source, startIndex: Int, tokenizerStateSetter: TokenizerStateSetter): Token
    {
        // initialize
        var index = startIndex
        val stringBuffer = StringBuffer()

        // while not EOF
        while (index < source.text.length)
        {
            // buffer characters
            stringBuffer.append(source.text[index++])
            index = this.tryConsumeString(source, stringBuffer, index)
            index = this.tryConsumeRawString(source, stringBuffer, index)

            // if buffer ends with tag terminator
            if (stringBuffer.endsWith("#>"))
            {
                // set state to text, and return
                tokenizerStateSetter(TextTokenizerState(this.tokenizerSettings))
                return EchoToken(
                    echo = stringBuffer.removeSuffix("#>").toString(),
                    location = Location(
                        name = source.name,
                        startIndex = startIndex,
                        untilIndex = index
                    )
                )
            }
        }

        // unexpected EOF
        throw this.getUnexpectedEOFTokenizerStateException(source, startIndex, index)
    }
}
