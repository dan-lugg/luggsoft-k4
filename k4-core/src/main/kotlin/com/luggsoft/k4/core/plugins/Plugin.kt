package com.luggsoft.k4.core.plugins

import com.luggsoft.k4.core.engine.EngineBuilder

interface Plugin
{
    val id: String

    fun apply(engineBuilder: EngineBuilder)
}
