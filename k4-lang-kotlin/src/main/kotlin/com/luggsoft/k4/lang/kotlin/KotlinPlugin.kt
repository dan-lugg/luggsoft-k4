package com.luggsoft.k4.lang.kotlin

import com.luggsoft.k4.core.engine.evaluators.Evaluator
import com.luggsoft.k4.core.engine.generators.Generator
import com.luggsoft.k4.core.engine.tokenizers.DefaultTokenizer
import com.luggsoft.k4.core.engine.tokenizers.Tokenizer
import com.luggsoft.k4.core.engine.tokenizers.TokenizerSettings
import com.luggsoft.k4.core.plugins.PluginBase
import com.luggsoft.k4.lang.kotlin.engine.evaluators.KotlinEvaluator
import com.luggsoft.k4.lang.kotlin.engine.generators.KotlinGenerator
import com.luggsoft.k4.lang.kotlin.engine.generators.formatters.KtLintFormatter

class KotlinPlugin : PluginBase(
    id = "lang-kotlin"
)
{
    override fun createTokenizer(tokenizerSettings: TokenizerSettings): Tokenizer = DefaultTokenizer(
        tokenizerSettings = tokenizerSettings
    )

    override fun createGenerator(): Generator = KotlinGenerator(
        formatter = KtLintFormatter()
    )

    override fun createEvaluator(): Evaluator = KotlinEvaluator()
}
