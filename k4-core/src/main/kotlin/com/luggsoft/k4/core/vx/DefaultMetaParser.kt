package com.luggsoft.k4.core.vx

import com.fasterxml.jackson.dataformat.yaml.YAMLMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule

class DefaultMetaParser(
    private val yamlMapper: YAMLMapper,
) : MetaParser
{
    override fun parseMeta(charSequence: CharSequence): Meta = this.yamlMapper
        .readValue(charSequence.toString())

    object Instance : MetaParser by DefaultMetaParser(
        yamlMapper = YAMLMapper().also { yamlMapper ->
            yamlMapper.registerKotlinModule()
            yamlMapper.findAndRegisterModules()
        },
    )
}
