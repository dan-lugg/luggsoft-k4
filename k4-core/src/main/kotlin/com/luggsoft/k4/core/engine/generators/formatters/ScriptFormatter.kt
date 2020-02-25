package com.luggsoft.k4.core.engine.generators.formatters

interface ScriptFormatter
{
    fun formatScript(script: String): String
}

interface ScriptFormatterSettings

abstract class ScriptFormatterBase<TScriptFormatterSettings : ScriptFormatterSettings>(
    scriptFormatterSettings: TScriptFormatterSettings
) : ScriptFormatter
{
    protected abstract val scriptFormatterSettings: TScriptFormatterSettings
}
