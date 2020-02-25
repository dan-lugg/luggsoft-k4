package com.luggsoft.k4.core.engine.tokenizers.tokens

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.luggsoft.k4.core.engine.Location

abstract class TokenBase(
    final override val location: Location
) : Token
{
    private val escapedContent: String
        get() = TokenBase.objectMapper.writeValueAsString(this.content)

    abstract val content: String

    override fun toString(): String = """${this::class.java.simpleName}[${this.location}]=${this.escapedContent}"""

    private companion object
    {
        @JvmStatic
        val objectMapper = jacksonObjectMapper()
    }
}
