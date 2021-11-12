package com.luggsoft.k4.core.templates

import com.luggsoft.k4.core.sources.Source
import org.slf4j.Logger
import java.io.Writer
import kotlin.reflect.KClass

interface Template<T : Any>
{
    val source: Source

    val script: String

    val modelKClass: KClass<T>

    fun execute(model: T, writer: Writer, logger: Logger)

    fun writeScript(writer: Writer)
}
