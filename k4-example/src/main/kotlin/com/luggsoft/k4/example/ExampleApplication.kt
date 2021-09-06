package com.luggsoft.k4.example

import com.luggsoft.k4.core.Source
import com.luggsoft.k4.core.engine.DefaultEngine
import com.luggsoft.k4.core.engine.evaluators.DefaultEvaluator
import com.luggsoft.k4.core.engine.generators.DefaultGenerator
import com.luggsoft.k4.core.engine.tokenizers.DefaultTokenizer
import com.luggsoft.k4.core.engine.tokenizers.TokenizerSettings
import javax.script.ScriptEngineManager

object ExampleApplication
{
    @JvmStatic
    fun main(vararg args: String)
    {
        val source = Source.fromResource("/KotlinApiClient.k4")
        val engine = DefaultEngine(
            tokenizer = DefaultTokenizer(
                tokenizerSettings = TokenizerSettings.Default
            ),
            generator = DefaultGenerator(
                // formatter = KtLintFormatter()
            ),
            evaluator = DefaultEvaluator(
                scriptEngineManager = ScriptEngineManager()
            )
        )

        val writer = System.out.writer()
        engine.compileAndExecute(source, writer, MyModel())
        writer.flush()
    }
}
