package com.luggsoft.k4.lang.kotlin

import com.luggsoft.k4.core.engine.tokens.TokenProvider
import javax.script.ScriptEngineManager

internal class DefaultKotlinScriptTemplateFactory(
    tokenProvider: TokenProvider,
    scriptEngineManager: ScriptEngineManager
) : KotlinScriptTemplateFactoryBase(
    tokenProvider = tokenProvider,
    scriptEngineManager = scriptEngineManager
)
