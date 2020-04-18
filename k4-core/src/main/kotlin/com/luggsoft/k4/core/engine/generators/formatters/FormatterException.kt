package com.luggsoft.k4.core.engine.generators.formatters

class FormatterException : Exception
{
    constructor() : super()
    constructor(message: String) : super(message)
    constructor(message: String, cause: Exception) : super(message, cause)
}
