package com.luggsoft.k4.core.vx.internal.generators

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.luggsoft.k4.core.vx.DefaultMetaParser
import com.luggsoft.k4.core.vx.Meta
import com.luggsoft.k4.core.vx.MetaParser
import com.luggsoft.k4.core.vx.internal.formatters.Formatter
import com.luggsoft.k4.core.vx.internal.formatters.DefaultFormatter
import com.luggsoft.k4.core.vx.internal.tokenizers.CommentToken
import com.luggsoft.k4.core.vx.internal.tokenizers.KotlinToken
import com.luggsoft.k4.core.vx.internal.tokenizers.MetaToken
import com.luggsoft.k4.core.vx.internal.tokenizers.PrintToken
import com.luggsoft.k4.core.vx.internal.tokenizers.RawToken
import com.luggsoft.k4.core.vx.internal.tokenizers.Token
import org.slf4j.Logger
import java.io.Writer
import java.lang.StringBuilder
import java.time.Instant

// tokenizer
// generator
// formatter
// templater

class DefaultGenerator(
    private val objectMapper: ObjectMapper,
    private val generatorFlags: GeneratorFlags,
    private val metaParser: MetaParser,
    private val formatter: Formatter,
) : Generator
{
    override fun generate(tokens: Iterable<Token>, writer: Writer)
    {
        val builder = StringBuilder()
        val metaToken = tokens.filterIsInstance<MetaToken>().singleOrNull()
        val bodyTokens = tokens.filterNot { token -> token is MetaToken }

        val meta = when (metaToken)
        {
            null -> Meta(
                modelType = Any::class.qualifiedName!!,
                packageName = "com.example",
                importsNames = emptyList(),
            )
            else -> this.metaParser.parseMeta(metaToken.value)
        }

        builder.appendLine("package ${meta.packageName ?: "template_${Instant.now().toEpochMilli()}"}")
        builder.appendLine("fun render(")
        builder.appendLine("model: ${meta.modelType},")
        builder.appendLine("output: ${Writer::class.qualifiedName},")
        builder.appendLine("logger: ${Logger::class.qualifiedName},")
        builder.appendLine(")")
        builder.appendLine("{")

        for (token in bodyTokens)
        {
            if (this.generatorFlags.getValue(GeneratorFlags.Flag.INCLUDE_SOURCE_MARKERS))
            {
                builder.appendLine("/// ${token.location.toRangeString()}")
            }

            for (line in this.generateLineSequence(token))
            {
                builder.appendLine(line)
            }
        }

        builder.appendLine("}")

        builder.toString()
            .let(this.formatter::format)
            .let(writer::append)
    }

    private fun generateLineSequence(token: Token): Sequence<String> = sequence {
        when (token)
        {
            is RawToken -> this.yield("output.appendLine(${this@DefaultGenerator.objectMapper.writeValueAsString(token.value)})")
            is MetaToken -> TODO()
            is PrintToken -> this.yield("output.appendLine(${token.value.trim()})")

            is KotlinToken ->
            {
                val lines = token.value.lineSequence()
                    .filterNot(String::isBlank)
                    .map(String::trim)
                this.yieldAll(lines)
            }

            is CommentToken ->
            {
                if (this@DefaultGenerator.generatorFlags.getValue(GeneratorFlags.Flag.LOG_COMMENTS))
                {
                    this.yield("logger.appendLine(${objectMapper.writeValueAsString(token.value)})")
                }
            }
        }
    }

    object Instance : Generator by DefaultGenerator(
        objectMapper = jacksonObjectMapper(),
        generatorFlags = GeneratorFlags.createDefault(),
        metaParser = DefaultMetaParser.Instance,
        formatter = DefaultFormatter(),
    )
}

