package com.luggsoft.k4.core

import java.io.InputStream
import java.io.Reader
import java.io.StringReader
import java.nio.charset.Charset
import kotlin.reflect.KClass
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper as YamlMapper

class YamlModelProvider(
    private val reader: Reader,
    private val yamlMapper: YamlMapper,
) : ModelProvider
{
    constructor(
        yaml: String,
        yamlMapper: YamlMapper,
    ) : this(
        reader = StringReader(yaml),
        yamlMapper = yamlMapper,
    )

    constructor(
        inputStream: InputStream,
        charset: Charset = Charsets.UTF_8,
        yamlMapper: YamlMapper,
    ) : this(
        reader = inputStream.reader(charset),
        yamlMapper = yamlMapper,
    )

    private val rootYamlNode = this.yamlMapper.readTree(this.reader)

    override fun <T : Any> getModel(name: String, kClass: KClass<T>): T
    {
        val modelYamlNode = this.rootYamlNode.get(name)
        return this.yamlMapper.convertValue(modelYamlNode, kClass.java)
    }
}
