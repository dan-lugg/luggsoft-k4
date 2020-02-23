package com.luggsoft.k4.core

interface TemplateFactory
{
    @Throws(TemplateFactoryException::class)
    fun createTemplate(sourceProvider: SourceProvider): Template
}

