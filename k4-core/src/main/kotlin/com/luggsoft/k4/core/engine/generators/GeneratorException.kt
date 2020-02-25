package com.luggsoft.k4.core.engine.generators

class GeneratorException : Exception
{
    constructor() : super()
    constructor(message: String) : super(message)
    constructor(message: String, cause: Throwable) : super(message, cause)
}
