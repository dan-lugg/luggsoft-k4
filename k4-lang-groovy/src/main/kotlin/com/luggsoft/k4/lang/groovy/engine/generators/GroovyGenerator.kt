package com.luggsoft.k4.lang.groovy.engine.generators

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.luggsoft.k4.core.engine.generators.Generator
import com.luggsoft.k4.core.engine.generators.GeneratorBase
import com.luggsoft.k4.core.engine.tokenizers.tokens.CodeToken
import com.luggsoft.k4.core.engine.tokenizers.tokens.EchoToken
import com.luggsoft.k4.core.engine.tokenizers.tokens.InfoToken
import com.luggsoft.k4.core.engine.tokenizers.tokens.TextToken
import com.luggsoft.k4.core.engine.tokenizers.tokens.Token

class GroovyGenerator : GeneratorBase()
{
    override fun generateScript(tokens: List<Token>): String
    {
        val headLines = tokens.filter { token -> token is InfoToken }.flatMap(this::getCodeLines)
        val bodyLines = tokens.filterNot { token -> token is InfoToken }.flatMap(this::getCodeLines)
        return this.getCodeLines(headLines, bodyLines)
            .map(String::trim)
            .filterNot(String::isEmpty)
            .joinToString(separator = "\n")
    }

    private fun getCodeLines(headLines: List<String>, bodyLines: List<String>): List<String>
    {
        return mutableListOf<String>().also { lines ->
            lines.addAll(headLines)
            lines.add("def render(Writer writer)")
            lines.add("{")
            lines.addAll(bodyLines)
            lines.add("}")
        }
    }

    private fun getCodeLines(token: Token): List<String>
    {
        return when (token)
        {
            is TextToken -> listOf("""writer.append(${objectMapper.writeValueAsString(token.text)})""")
            is EchoToken -> listOf("""writer.append(${token.echo.trim()})""")
            is CodeToken -> token.code.lines()
            else -> listOf("""/* $token */""")
        }
    }

    private companion object
    {
        val objectMapper: ObjectMapper = jacksonObjectMapper()
    }

    internal object Default : Generator by GroovyGenerator()
}
