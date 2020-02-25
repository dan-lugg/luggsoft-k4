package com.luggsoft.k4.lang.groovy

import com.luggsoft.k4.core.engine.evaluators.Evaluator
import com.luggsoft.k4.core.engine.generators.Generator
import com.luggsoft.k4.core.engine.tokenizers.DefaultTokenizer
import com.luggsoft.k4.core.engine.tokenizers.Tokenizer
import com.luggsoft.k4.core.engine.tokenizers.TokenizerSettings
import com.luggsoft.k4.core.plugins.PluginBase
import com.luggsoft.k4.lang.groovy.engine.evaluators.GroovyEvaluator
import com.luggsoft.k4.lang.groovy.engine.generators.GroovyGenerator

class GroovyPlugin : PluginBase(
    id = "lang-groovy"
)
{
    override fun createTokenizer(tokenizerSettings: TokenizerSettings): Tokenizer = DefaultTokenizer(
        tokenizerSettings = tokenizerSettings
    )

    override fun createGenerator(): Generator = GroovyGenerator()

    override fun createEvaluator(): Evaluator = GroovyEvaluator()
}
