package com.luggsoft.k4.core.engine.tokenizers.tokens

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.luggsoft.k4.core.engine.Location

abstract class TokenBase(
    final override val location: Location
) : Token
{
    private val escapedContent: String
        get() = TokenBase.objectMapper.writeValueAsString(this.content)

    final override fun toString(): String = """${this::class.java.simpleName}[${this.location}]=${this.escapedContent}"""

    final override fun equals(other: Any?): Boolean
    {
        if (other is Token)
        {
            if (this::class != other::class)
            {
                println("not token type")
                return false
            }

            println("this.content = ${this.content}")
            println("that.content = ${other.content}")

            println("this.location = ${this.location}")
            println("that.location = ${other.location}")

            return (this.content == other.content) && (this.location == other.location)
        }

        println("not token")
        return false
    }

    private companion object
    {
        @JvmStatic
        val objectMapper = jacksonObjectMapper()
    }
}
