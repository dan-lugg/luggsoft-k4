package com.luggsoft.k4.core.engine

import com.luggsoft.k4.core.Source
import com.luggsoft.k4.core.Template
import com.luggsoft.k4.core.engine.evaluators.DefaultEvaluator
import com.luggsoft.k4.core.engine.evaluators.Evaluator
import com.luggsoft.k4.core.engine.generators.DefaultGenerator
import com.luggsoft.k4.core.engine.generators.Generator
import com.luggsoft.k4.core.engine.generators.Script
import com.luggsoft.k4.core.engine.tokenizers.DefaultTokenizer
import com.luggsoft.k4.core.engine.tokenizers.Tokenizer
import java.io.Writer

class DefaultEngine(
    private val tokenizer: Tokenizer,
    private val generator: Generator,
    private val evaluator: Evaluator
) : Engine
{
    @Throws(EngineException::class)
    override fun compileToScript(source: Source): Script
    {
        try
        {
            val tokens = this.tokenizer.tokenizeSource(source)
            return this.generator.generateScript(tokens)
        }
        catch (exception: Exception)
        {
            throw EngineException("Failed to compile to script", exception)
        }
    }

    @Throws(EngineException::class)
    override fun compileToTemplate(source: Source): Template
    {
        try
        {
            val tokens = this.tokenizer.tokenizeSource(source)
            val script = this.generator.generateScript(tokens)
            return this.evaluator.evaluateScript(script)
        }
        catch (exception: Exception)
        {
            throw EngineException("Failed to compile to template", exception)
        }
    }

    @Throws(EngineException::class)
    override fun compileAndExecute(source: Source, writer: Writer, model: Any?)
    {
        try
        {
            val tokens = this.tokenizer.tokenizeSource(source)
            val script = this.generator.generateScript(tokens)
            val template = this.evaluator.evaluateScript(script)
            val rendered = template.render(model)
            writer.write(rendered)
        }
        catch (exception: Exception)
        {
            throw EngineException("Failed to compile and execute", exception)
        }
    }

    object Instance : Engine by DefaultEngine(
        tokenizer = DefaultTokenizer.Instance,
        generator = DefaultGenerator.Instance,
        evaluator = DefaultEvaluator.Instance
    )
}

