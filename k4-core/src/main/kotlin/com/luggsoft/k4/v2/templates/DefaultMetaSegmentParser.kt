package com.luggsoft.k4.v2.templates

import com.fasterxml.jackson.dataformat.yaml.YAMLMapper
import com.fasterxml.jackson.module.kotlin.convertValue
import com.luggsoft.k4.v2.sources.segments.MetaTagSegment

class DefaultMetaSegmentParser(
    private val yamlMapper: YAMLMapper,
) : MetaSegmentParser
{
    override fun parseMetaSegments(metaTagSegments: List<MetaTagSegment>): Meta
    {
        var metaNode = this.yamlMapper.createObjectNode()
        val metaNodeUpdater = this.yamlMapper.readerForUpdating(metaNode)

        for (metaTagSegment in metaTagSegments)
        {
            if (metaTagSegment.content.isBlank())
            {
                continue
            }

            val node = this.yamlMapper.readTree(metaTagSegment.content)
            metaNode = metaNodeUpdater.readValue(node)
        }

        return this.yamlMapper.convertValue(metaNode)
    }

    companion object Instance : MetaSegmentParser by DefaultMetaSegmentParser(
        yamlMapper = YAMLMapper.builder()
            .findAndAddModules()
            .build(),
    )
}
