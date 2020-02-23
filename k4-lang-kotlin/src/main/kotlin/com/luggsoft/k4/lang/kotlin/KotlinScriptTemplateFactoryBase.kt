package com.luggsoft.k4.lang.kotlin

import com.luggsoft.k4.core.ScriptTemplateFactoryBase
import com.luggsoft.k4.core.engine.tokens.Token
import com.luggsoft.k4.core.engine.tokens.TokenProvider
import javax.script.ScriptEngineManager

abstract class KotlinScriptTemplateFactoryBase(
    tokenProvider: TokenProvider,
    scriptEngineManager: ScriptEngineManager
) : ScriptTemplateFactoryBase(
    tokenProvider = tokenProvider,
    scriptEngineManager = scriptEngineManager
)
{
    override val scriptEngineName: String get() = SCRIPT_ENGINE_NAME

    override fun parseScript(tokens: List<Token>): String = TODO()
}
