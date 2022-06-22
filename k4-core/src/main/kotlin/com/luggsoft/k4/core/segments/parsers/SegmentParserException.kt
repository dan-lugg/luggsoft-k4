package com.luggsoft.k4.core.segments.parsers

class SegmentParserException : RuntimeException
{
    constructor() : super()
    constructor(message: String) : super(message)
    constructor(message: String, cause: Throwable) : super(message, cause)
}
