package com.luggsoft.k4.core.engine

import com.luggsoft.k4.core.plugins.DefaultPluginLoader
import com.luggsoft.k4.core.plugins.PluginLoader

abstract class EngineFactoryBase(
    private val pluginLoader: PluginLoader = DefaultPluginLoader(
        classLoader = ClassLoader.getSystemClassLoader()
    )
) : EngineFactory
{
    final override fun createEngine(): Engine
    {
        val engineBuilder = this.createEngineBuilder()
        val plugins = this.pluginLoader.loadPlugins()
        for (plugin in plugins)
        {
            plugin.apply(engineBuilder)
        }
        return engineBuilder.build()
    }

    protected abstract fun createEngineBuilder(): EngineBuilder
}
