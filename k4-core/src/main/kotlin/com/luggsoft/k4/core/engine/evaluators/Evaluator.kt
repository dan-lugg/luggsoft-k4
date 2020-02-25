package com.luggsoft.k4.core.engine.evaluators

import com.luggsoft.k4.core.Template

interface Evaluator
{
    fun evaluateScript(input: String): Template
}
