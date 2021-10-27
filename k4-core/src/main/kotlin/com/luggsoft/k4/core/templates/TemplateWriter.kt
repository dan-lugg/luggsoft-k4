package com.luggsoft.k4.core.templates

import java.io.Writer

interface TemplateWriter
{
    fun write(value: Any?)

    companion object
    {
        fun createDefault(writer: Writer) = object : TemplateWriter
        {
            override fun write(value: Any?)
            {
                writer.append("$value")
                writer.flush()
            }
        }
    }
}
