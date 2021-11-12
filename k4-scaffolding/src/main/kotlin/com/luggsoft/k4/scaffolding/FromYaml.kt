package com.luggsoft.k4.scaffolding

import com.fasterxml.jackson.dataformat.yaml.YAMLMapper
import com.luggsoft.k4.core.Engine
import com.luggsoft.k4.core.sources.DefaultSource
import com.luggsoft.k4.core.templates.Template

fun DefaultTemplateRegistry.Companion.fromYaml(yaml: String, yamlMapper: YAMLMapper, engine: Engine): TemplateRegistry
{
    val nameTemplateMap = mutableMapOf<String, Template<Any>>()
    for (yamlNode in yamlMapper.readTree(yaml))
    {
        val name = yamlNode.get("name").asText()
        val content = yamlNode.get("code").asText()
        val source = DefaultSource(name, content)
        nameTemplateMap[name] = engine.compile(source)
    }
    return DefaultTemplateRegistry(nameTemplateMap)
}
