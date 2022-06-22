package com.luggsoft.k4.core

import com.luggsoft.k4.core.segments.Segment
import java.io.Writer
import kotlin.reflect.KClass

interface TemplateWriter
{
    fun <TModel : Any> writeTemplate(writer: Writer, segments: Iterable<Segment>, modelKClass: KClass<TModel>)
}
