package com.luggsoft.k4.core.engine.evaluators

import com.luggsoft.k4.core.Template
import com.luggsoft.k4.core.io.CodeBuilder
import javax.script.Invocable
import javax.script.ScriptEngineManager

abstract class EvaluatorBase(
    private val scriptEngineManager: ScriptEngineManager
) : Evaluator
{
    protected abstract val scriptEngineName: String

    final override fun evaluateScript(input: String): Template
    {
        val scriptEngine = this.scriptEngineManager.getEngineByName(this.scriptEngineName)
        scriptEngine.eval(input)

        return object : Template
        {
            private val invocable: Invocable = scriptEngine as Invocable

            override fun render(model: Any?): String
            {
                val codeBuilder = CodeBuilder()
                this.invocable.invokeFunction("render", codeBuilder)
                return codeBuilder.toString()
            }
        }
    }
}
