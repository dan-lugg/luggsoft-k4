package com.luggsoft.k4.lang.groovy.engine.evaluators

import com.luggsoft.k4.core.engine.evaluators.EvaluatorBase
import javax.script.ScriptEngineManager

class GroovyEvaluator : EvaluatorBase(
    scriptEngineManager = ScriptEngineManager()
)
{
    override val scriptEngineName: String = "groovy"
}
