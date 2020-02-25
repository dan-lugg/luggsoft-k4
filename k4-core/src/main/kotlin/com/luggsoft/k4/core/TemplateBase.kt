package com.luggsoft.k4.core

import com.luggsoft.common.logger
import com.luggsoft.k4.core.io.CodeBuilder

abstract class TemplateBase(
    private val modelClass: Class<*>
) : Template
{
    @Throws(TemplateException::class)
    final override fun render(model: Any?): String
    {
        try
        {
            val codeBuilder = CodeBuilder()
            this.execute(codeBuilder, model)
            return codeBuilder.toString()
        }
        catch (exception: Exception)
        {
            ("Failed to render template").also { message ->
                this.logger.error(message, exception)
                throw TemplateException(message, exception)
            }
        }
    }

    protected abstract fun execute(codeBuilder: CodeBuilder, model: Any?)
}
