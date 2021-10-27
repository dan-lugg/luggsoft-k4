package com.luggsoft.k4.core.engine.generators

import com.fasterxml.jackson.dataformat.yaml.YAMLMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.luggsoft.common.EMPTY_STRING
import com.luggsoft.common.logger
import com.luggsoft.k4.core.engine.generators.formatters.Formatter
import com.luggsoft.k4.core.engine.generators.formatters.UnindentFormatter
import com.luggsoft.k4.core.engine.tokenizers.tokens.BodyToken
import com.luggsoft.k4.core.engine.tokenizers.tokens.CodeToken
import com.luggsoft.k4.core.engine.tokenizers.tokens.EchoToken
import com.luggsoft.k4.core.engine.tokenizers.tokens.InfoToken
import com.luggsoft.k4.core.engine.tokenizers.tokens.NoteToken
import com.luggsoft.k4.core.engine.tokenizers.tokens.TextToken
import com.luggsoft.k4.core.engine.tokenizers.tokens.Token
import com.luggsoft.k4.core.internal.OBJECT_MAPPER
import com.luggsoft.k4.core.io.CodeBuilder
import java.nio.ByteBuffer
import java.util.*

class DefaultGenerator(
    private val formatter: Formatter,
) : Generator
{
    override fun generateScript(tokens: List<Token>): Script
    {
        val yamlMapper = YAMLMapper()
            .findAndRegisterModules()

        val scriptInfo = tokens
            .filterIsInstance<InfoToken>()
            .single()
            .info.trimIndent()
            .let { info -> yamlMapper.readValue<ScriptInfo>(info) }

        return this.createScriptCode(scriptInfo, tokens)
            .let(this.formatter::format)
            .let { code ->
                this.logger.info("$this generated: $code")
                val modelClass = Class.forName(scriptInfo.modelClassName)
                return@let DefaultScript(
                    name = scriptInfo.name ?: "Script${this.getUniqueName()}",
                    code = code,
                    modelClass = modelClass.kotlin
                )
            }
    }

    @Suppress("NAME_SHADOWING")
    private fun createScriptCode(scriptInfo: ScriptInfo, tokens: List<Token>): String
    {
        val modelClass = Class.forName(scriptInfo.modelClassName)
        val infoTokens = tokens.filterIsInstance<InfoToken>()
        val bodyTokens = tokens.filterIsInstance<BodyToken>()
        val functionTokens = tokens
            .filterNot { token -> token is InfoToken }
            .filterNot { token -> token is BodyToken }
            .filterNot { token -> token is NoteToken }

        fun renderImports(): String?
        {
            return scriptInfo.importsElementNames
                ?.joinToString(separator = "\n") { name -> "import $name" }
                .orEmpty()
        }

        fun renderHeader(): String
        {
            return """
                package ${scriptInfo.packageName ?: "package_${this.getUniqueName()}"}
                
                import ${CodeBuilder::class.qualifiedName}
                import ${modelClass.kotlin.qualifiedName}
                ${renderImports()}
            """
        }

        fun renderFooter(): String
        {
            return """
                ${bodyTokens.joinToString(separator = "\n", transform = BodyToken::body)}    
            """

        }

        fun renderFunctionBody(): String
        {
            return functionTokens.flatMap(this::getTokenCodeLines).joinToString(separator = "\n")
        }

        fun renderFunction(): String
        {

            return """
                ${renderHeader()}
                
                
                fun render(codeBuilder: ${CodeBuilder::class.simpleName}, model: ${modelClass.kotlin.simpleName})
                {
                    ${renderFunctionBody()}
                }
                
                ${renderFooter()}
            """
        }

        return renderFunction().trimIndent()
    }

    private fun getTokenCodeLines(token: Token): List<String>
    {
        return this.getTokenCode(token).lines().filterNot(String::isBlank)
    }

    private fun getTokenCode(token: Token): String
    {
        return when (token)
        {
            is TextToken -> """codeBuilder.append(${OBJECT_MAPPER.writeValueAsString(token.text)})"""
            is EchoToken -> """codeBuilder.append(${token.echo.trim()})"""
            is CodeToken -> token.code
            else -> EMPTY_STRING
        }
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

    internal data class ScriptInfo(
        val name: String?,
        val packageName: String?,
        val modelClassName: String?,
        val extendsClassName: String?,
        val importsElementNames: List<String>?
    )

    object Instance : Generator by DefaultGenerator(
        formatter = UnindentFormatter(),
    )
}
