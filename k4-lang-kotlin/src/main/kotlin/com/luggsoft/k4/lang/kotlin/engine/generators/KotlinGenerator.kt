package com.luggsoft.k4.lang.kotlin.engine.generators

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.luggsoft.k4.core.engine.generators.Generator
import com.luggsoft.k4.core.engine.generators.GeneratorBase
import com.luggsoft.k4.core.io.CodeBuilder
import com.luggsoft.k4.core.engine.tokenizers.tokens.CodeToken
import com.luggsoft.k4.core.engine.tokenizers.tokens.EchoToken
import com.luggsoft.k4.core.engine.tokenizers.tokens.InfoToken
import com.luggsoft.k4.core.engine.tokenizers.tokens.TextToken
import com.luggsoft.k4.core.engine.tokenizers.tokens.Token
import com.luggsoft.k4.lang.kotlin.engine.generators.formatters.Formatter
import com.luggsoft.k4.lang.kotlin.engine.generators.formatters.KtLintFormatter
import com.luggsoft.k4.lang.kotlin.engine.generators.kotlinpoet.addCode
import com.luggsoft.k4.lang.kotlin.engine.generators.kotlinpoet.addFunction
import com.luggsoft.k4.lang.kotlin.engine.generators.kotlinpoet.addParameter
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import java.nio.ByteBuffer
import java.util.*

const val EMPTY_STRING: String = ""

class KotlinGenerator(
    private val formatter: Formatter
) : GeneratorBase()
{
    override fun generateScript(tokens: List<Token>): String
    {
        /*
        val headLines = tokens.filter { token -> token is InfoToken }.flatMap(this::getCodeLines)
        val bodyLines = tokens.filterNot { token -> token is InfoToken }.flatMap(this::getCodeLines)
        return this.getCodeLines(headLines, bodyLines)
            .map(String::trim)
            .filterNot(String::isEmpty)
            .joinToString(separator = "\n")
            .let(this.formatter::format)
        */
        val kotlinFileInfo = KotlinFileInfo(
            packageName = "com.example.test",
            extendsClassName = "com.example.test.TemplateBase",
            importsElementNames = listOf(
                "java.util.UUID"
            ),
            tokens = tokens
        )
        return this.createFileSpec(kotlinFileInfo)
            .toString()
            .lines()
            .joinToString(separator = "\n")
            .let(this.formatter::format)
    }

    private fun getUniqueName(): String
    {
        return UUID.randomUUID()
            .let { uuid ->
                return@let ByteBuffer.allocate(16)
                    .putLong(uuid.leastSignificantBits)
                    .putLong(uuid.mostSignificantBits)
                    .array()
            }
            .joinToString(separator = EMPTY_STRING) { byte ->
                return@joinToString String.format("%02x", byte)
            }
    }

    @Suppress("NAME_SHADOWING")
    private fun createFileSpec(kotlinFileInfo: KotlinFileInfo): FileSpec
    {
        val name = this.getUniqueName()
        val fileName = "Template$name.kt"
        return FileSpec.builder(kotlinFileInfo.packageName, fileName)
            .apply {
                kotlinFileInfo.importsElementNames.forEach { name ->
                    val packageName = name.substringAfterLast('.')
                    val elementName = name.substringBeforeLast('.')
                    this.addImport(packageName, elementName)
                }
            }
            .addFunction("render") {
                this.addParameter("codeBuilder", CodeBuilder::class) {
                    this.defaultValue("%L", "TODO()")
                }
                this.addCode {
                    val bodyLines = kotlinFileInfo.tokens
                        .filterNot { token -> token is InfoToken }
                        .flatMap(this@KotlinGenerator::getCodeLines)
                    bodyLines.forEach { line -> this.addStatement(line) }
                }
            }
            .build()
    }

    private fun getCodeLines(token: Token): List<String>
    {
        return this.getCodeBlock(token).toString().lines().filterNot(String::isBlank)
    }

    private fun getCodeBlock(token: Token): CodeBlock
    {
        return when (token)
        {
            is TextToken -> CodeBlock.builder()
                .addStatement("""codeBuilder.append(${objectMapper.writeValueAsString(token.text)})""")
                .build()
            is EchoToken -> CodeBlock.builder()
                .addStatement("""codeBuilder.append(${token.echo.trim()})""")
                .build()
            is CodeToken -> CodeBlock.builder()
                .addStatement(token.code)
                .build()
            else -> CodeBlock.builder()
                .addStatement(EMPTY_STRING)
                .build()
        }
    }

    private data class KotlinFileInfo(
        val packageName: String,
        val extendsClassName: String,
        val importsElementNames: List<String>,
        val tokens: List<Token>
    )

    private companion object
    {
        val objectMapper: ObjectMapper = jacksonObjectMapper()
    }

    internal object Default : Generator by KotlinGenerator(
        formatter = KtLintFormatter()
    )
}
