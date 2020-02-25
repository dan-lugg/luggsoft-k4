package com.luggsoft.k4.core

class TemplateException : Exception
{
    constructor() : super()
    constructor(message: String) : super(message)
    constructor(message: String, cause: Exception) : super(message, cause)
}
