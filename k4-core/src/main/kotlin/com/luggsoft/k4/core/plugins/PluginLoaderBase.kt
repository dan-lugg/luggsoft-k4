package com.luggsoft.k4.core.plugins

import java.util.*

abstract class PluginLoaderBase(
    private val classLoader: ClassLoader
) : PluginLoader
{
    final override fun loadPlugins(): List<Plugin> = ServiceLoader.load(Plugin::class.java)
        .toList()
}
