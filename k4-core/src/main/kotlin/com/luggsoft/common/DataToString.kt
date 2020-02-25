package com.luggsoft.common

import kotlin.reflect.full.memberProperties

inline fun <reified TThis : Any> TThis.dataToString(): String = TThis::class.memberProperties
    .associate { kProperty1 -> kProperty1.name to kProperty1.get(this) }
    .let { properties -> "${TThis::class.java.name}: $properties" }
