package com.luggsoft.k4.core.engine.tokens

class TokenProviderStateException : Exception
{
    constructor() : super()
    constructor(message: String) : super(message)
    constructor(message: String, cause: Throwable) : super(message, cause)
}
