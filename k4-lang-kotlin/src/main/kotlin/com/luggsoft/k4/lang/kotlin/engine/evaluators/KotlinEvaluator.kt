package com.luggsoft.k4.lang.kotlin.engine.evaluators

import com.luggsoft.k4.core.engine.evaluators.EvaluatorBase
import javax.script.ScriptEngineManager

class KotlinEvaluator : EvaluatorBase(
    scriptEngineManager = ScriptEngineManager()
)
{
    override val scriptEngineName: String = "kotlin"
}
