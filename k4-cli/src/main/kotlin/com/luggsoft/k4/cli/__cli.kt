package com.luggsoft.k4.cli

import com.luggsoft.k4.core.Source
import com.luggsoft.k4.core.engine.tokenizers.TokenizerSettings
import com.luggsoft.k4.core.plugins.PluginBase
import com.luggsoft.k4.core.plugins.PluginLoader

fun main()
{
    val plugin = PluginLoader.Default.loadPlugins()
        .filterIsInstance<PluginBase>()
        .first { plugin -> plugin.id == "lang-kotlin" }

    val tokenizer = plugin.createTokenizer(
        tokenizerSettings = TokenizerSettings.Default
    )
    val generator = plugin.createGenerator()
    val evaluator = plugin.createEvaluator()

    val sourceProvider = Source.fromResource(
        name = "/examples/example1.k4"
    )

    println("--- TOKENIZE SOURCE ---")
    val tokens = tokenizer.tokenizeSource(sourceProvider, 0)
    tokens.forEach { token -> println(token) }
    println()

    println("--- GENERATE SCRIPT ---")
    val script = generator.generateScript(tokens)
    println(script)
    println()

    println("--- EVALUATE SCRIPT ---")
    val template = evaluator.evaluateScript(script)
    println(template)
    println()

    template.render().also(::println)
}
