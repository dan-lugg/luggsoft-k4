package com.luggsoft.k4.core.plugins

interface PluginLoader
{
    fun loadPlugins(): List<Plugin>

    object Default : PluginLoader by DefaultPluginLoader(
        classLoader = ClassLoader.getSystemClassLoader()
    )
}
