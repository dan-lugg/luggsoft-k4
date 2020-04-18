package com.luggsoft.k4.core.engine.evaluators

import com.luggsoft.k4.core.Template
import com.luggsoft.k4.core.engine.generators.Script

interface Evaluator
{
    fun evaluateScript(script: Script): Template
}
