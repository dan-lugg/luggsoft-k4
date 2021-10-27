package com.luggsoft.k4.core.templates

import com.fasterxml.jackson.annotation.JsonProperty
import kotlin.reflect.KCallable
import kotlin.reflect.KClass

data class Meta(
    @JsonProperty("name")
    val name: String?,

    @JsonProperty("model-type")
    val modelTypeName: String?,

    @JsonProperty("model-param")
    val modelParamName: String? = "model",

    @JsonProperty("import-types")
    val importTypeNames: List<String> = emptyList(),

    @JsonProperty("template-base-type")
    val templateBaseTypeName: String = Template::class.qualifiedName!!,

    @JsonProperty("template-writer-type")
    val templateWriterTypeName: String = TemplateWriter::class.qualifiedName!!,

    @JsonProperty("template-logger-type")
    val templateLoggerTypeName: String = TemplateLogger::class.qualifiedName!!,

    @JsonProperty("template-writer-method")
    val templateWriterMethodName: String = TemplateWriter::write.name,

    @JsonProperty("template-logger-method")
    val templateLoggerMethodName: String = TemplateLogger::log.name,
)
{
    val modelKClass: KClass<*>
        get() = when (this.modelTypeName)
        {
            null -> Object::class
            else -> Class.forName(this.modelTypeName).kotlin
        }

    val templateBaseKClass: KClass<*>
        get() = Class.forName(this.templateBaseTypeName).kotlin

    val templateWriterKClass: KClass<*>
        get() = Class.forName(this.templateWriterTypeName).kotlin

    val templateLoggerKClass: KClass<*>
        get() = Class.forName(this.templateLoggerTypeName).kotlin

    val templateWriterKCallable: KCallable<*> = this.templateWriterKClass.members
        .first { kCallable -> kCallable.name == this.templateWriterMethodName }

    val templateLoggerKCallable: KCallable<*> = this.templateLoggerKClass.members
        .first { kCallable -> kCallable.name == this.templateLoggerMethodName }
}
