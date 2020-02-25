package com.luggsoft.k4.core.engine.tokenizers.tokens

class TokenizerStateException : Exception
{
    constructor() : super()
    constructor(message: String) : super(message)
    constructor(message: String, cause: Throwable) : super(message, cause)
}
