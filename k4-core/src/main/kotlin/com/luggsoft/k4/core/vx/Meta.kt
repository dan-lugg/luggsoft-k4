package com.luggsoft.k4.core.vx

import com.luggsoft.k4.core.vx.util.kClassForName
import kotlin.reflect.KClass

data class Meta(
    val modelType: String,
    val packageName: String? = null,
    val importsNames: List<String>? = null,
)
{
    val modelTypeAsKClass: KClass<*>
        get() = kClassForName(this.modelType)
}
