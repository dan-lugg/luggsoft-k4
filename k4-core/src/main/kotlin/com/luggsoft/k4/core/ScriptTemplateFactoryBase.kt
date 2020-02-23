package com.luggsoft.k4.core

import com.luggsoft.k4.core.engine.tokens.Token
import com.luggsoft.k4.core.engine.tokens.TokenProvider
import com.luggsoft.k4.core.util.logger
import javax.script.Invocable
import javax.script.ScriptEngineManager

abstract class ScriptTemplateFactoryBase(
    private val tokenProvider: TokenProvider,
    private val scriptEngineManager: ScriptEngineManager
) : TemplateFactoryBase()
{
    protected abstract val scriptEngineName: String

    override fun createTemplate(sourceProvider: SourceProvider): Template
    {
        try
        {
            val tokens = this.tokenProvider.getTokens(sourceProvider)
            val script = this.parseScript(tokens)
            val scriptEngine = this.scriptEngineManager.getEngineByName(this.scriptEngineName)
            scriptEngine.eval(script)
            val invocable = scriptEngine as Invocable
            return invocable.getInterface(Template::class.java)
        }
        catch (exception: Exception)
        {
            ("Failed to create template").also { message ->
                this.logger.error(message, exception)
                throw TemplateFactoryException(message, exception)
            }
        }
    }

    protected abstract fun parseScript(tokens: List<Token>): String
}
