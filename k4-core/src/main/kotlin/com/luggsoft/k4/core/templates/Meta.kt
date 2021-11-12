package com.luggsoft.k4.core.templates

import com.fasterxml.jackson.annotation.JsonProperty
import kotlin.reflect.KClass

data class Meta(
    @JsonProperty("template")
    val templateName: String?,

    @JsonProperty("model-type")
    val modelTypeName: String?,

    @JsonProperty("model-param")
    val modelParamName: String? = "model",

    @JsonProperty("import-types")
    val importTypeNames: List<String> = emptyList(),

    @JsonProperty("template-base-type")
    val templateBaseTypeName: String = Template::class.qualifiedName!!,
)
{
    val modelKClass: KClass<*>
        get() = when (this.modelTypeName)
        {
            null -> Object::class
            else -> run {
                try
                {
                    return@run Class.forName(this.modelTypeName).kotlin
                }
                catch (exception: ClassNotFoundException)
                {
                    TODO("Unable to resolve class name ${this.modelTypeName}")
                }
            }
        }

    val templateBaseKClass: KClass<*>
        get() = Class.forName(this.templateBaseTypeName).kotlin
}
