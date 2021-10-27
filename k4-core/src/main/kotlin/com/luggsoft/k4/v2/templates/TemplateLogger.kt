package com.luggsoft.k4.v2.templates

import org.slf4j.LoggerFactory

interface TemplateLogger
{
    fun log(value: Any?)

    companion object
    {
        fun createDefault(template: Template) = object : TemplateLogger
        {
            private val logger = LoggerFactory.getLogger(template::class.java)

            override fun log(value: Any?)
            {
                this.logger.info("$value")
            }
        }
    }
}
