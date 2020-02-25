package com.luggsoft.k4.core.plugins

import com.luggsoft.k4.core.engine.EngineBuilder
import com.luggsoft.k4.core.engine.evaluators.Evaluator
import com.luggsoft.k4.core.engine.generators.Generator
import com.luggsoft.k4.core.engine.tokenizers.Tokenizer
import com.luggsoft.k4.core.engine.tokenizers.TokenizerSettings

abstract class PluginBase(
    final override val id: String
) : Plugin
{
    final override fun apply(engineBuilder: EngineBuilder): Unit = TODO()

    abstract fun createTokenizer(tokenizerSettings: TokenizerSettings): Tokenizer

    abstract fun createGenerator(): Generator

    abstract fun createEvaluator(): Evaluator
}
