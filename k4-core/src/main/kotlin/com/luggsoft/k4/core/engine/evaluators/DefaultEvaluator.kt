package com.luggsoft.k4.core.engine.evaluators

import com.luggsoft.common.logger
import com.luggsoft.k4.core.Template
import com.luggsoft.k4.core.TemplateBase
import com.luggsoft.k4.core.engine.generators.Script
import com.luggsoft.k4.core.io.CodeBuilder
import javax.script.Invocable
import javax.script.ScriptEngineManager
import javax.script.ScriptException

class DefaultEvaluator(
    private val scriptEngineManager: ScriptEngineManager
) : Evaluator
{
    @Throws(EvaluatorException::class)
    override fun evaluateScript(script: Script): Template
    {
        try
        {
            val scriptEngine = this.scriptEngineManager.getEngineByName("kotlin")
            scriptEngine.eval(script.code)
            return ScriptTemplate(script, scriptEngine as Invocable)
        }
        catch (exception: ScriptException)
        {
            ("Failed to evaluate script, because ${exception.message} at ${exception.lineNumber}:${exception.columnNumber} in ${exception.fileName}").also { message ->
                this.logger.error(message, exception)
                throw EvaluatorException(message, exception)
            }
        }
        catch (exception: Exception)
        {
            ("Failed to evaluate script").also { message ->
                this.logger.error(message, exception)
                throw EvaluatorException(message, exception)
            }
        }
    }

    private class ScriptTemplate(
        private val script: Script,
        private val invocable: Invocable
    ) : TemplateBase()
    {
        override fun execute(codeBuilder: CodeBuilder, model: Any?)
        {
            try
            {
                this.invocable.invokeFunction("render", codeBuilder, model)
            }
            catch (exception: NoSuchMethodException)
            {
                val expectModelClass = script.modelClass
                val actualModelClass = model!!::class
                ("No render method defined for model class $actualModelClass, expected $expectModelClass").also { message ->
                    this.logger.error(message, exception)
                    throw EvaluatorException(message, exception)
                }
            }
            catch (exception: ScriptException)
            {
                ("Failed to evaluate script, because ${exception.message} at ${exception.lineNumber}:${exception.columnNumber} in ${exception.fileName}").also { message ->
                    this.logger.error(message, exception)
                    throw EvaluatorException(message, exception)
                }
            }
        }
    }

    object Instance : Evaluator by DefaultEvaluator(
        scriptEngineManager = ScriptEngineManager()
    )
}
