package com.luggsoft.k4.scaffolding

import com.luggsoft.k4.core.Engine
import com.luggsoft.k4.core.sources.Source
import com.luggsoft.k4.core.templates.Template
import java.io.File
import java.nio.file.Path
import java.nio.file.Paths
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper as YamlMapper

interface NameTemplateMapBuilder
{
    fun buildNameTemplateMap(): Map<String, Template<Any>>
}

class DefaultNameTemplateMapBuilder(
    nameTemplateMap: Map<String, Template<Any>>,
) : NameTemplateMapBuilder
{
    private val nameTemplateMap: MutableMap<String, Template<Any>> = nameTemplateMap.toMutableMap()

    fun compileFromYaml(yaml: String, yamlMapper: YamlMapper, engine: Engine)
    {
        TODO()
    }

    fun compileFromPath(path: Path, engine: Engine)
    {
        val root = path.toFile()

        for (file in root.walkTopDown())
        {
            if (file.isDirectory)
            {
                continue
            }

            val currentPath = Paths.get(file.absolutePath)
            val relativePath = path.relativize(currentPath).normalize().toUnixString()

            this.nameTemplateMap[relativePath] = engine.compile(
                source = Source.fromFile(file)
            )
        }
    }

    override fun buildNameTemplateMap(): Map<String, Template<Any>> = this.nameTemplateMap.toMap()
}

class DefaultTemplateRegistry(
    nameTemplateMap: Map<String, Template<Any>>,
) : TemplateRegistry
{
    private val nameTemplateMap: MutableMap<String, Template<Any>> = nameTemplateMap.toMutableMap()

    override fun getTemplate(name: String): Template<Any>
    {
        @Suppress("UNCHECKED_CAST")
        return (this.nameTemplateMap[name] as Template<Any>)
    }

    override fun putTemplate(name: String, template: Template<Any>)
    {
        this.nameTemplateMap[name] = template
    }

    override fun iterator(): Iterator<Map.Entry<String, Template<Any>>> = this.nameTemplateMap.toMap().iterator()

    companion object
    {
        fun createFromDirectory(directoryName: String, engine: Engine): TemplateRegistry
        {
            val root = File(directoryName)
            val rootPath = Paths.get(root.absolutePath)
            val nameTemplateMap = mutableMapOf<String, Template<Any>>()

            for (file in root.walkTopDown())
            {
                if (file.isDirectory) continue
                if (file.extension != "k4") continue

                val currentPath = Paths.get(file.absolutePath)
                val relativePath = rootPath.relativize(currentPath).normalize().toUnixString()

                nameTemplateMap[relativePath] = engine.compile(
                    source = Source.fromFile(file)
                )
            }

            return DefaultTemplateRegistry(
                nameTemplateMap = nameTemplateMap,
            )
        }
    }
}
