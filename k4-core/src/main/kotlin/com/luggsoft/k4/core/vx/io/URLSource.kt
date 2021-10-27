package com.luggsoft.k4.core.vx.io

import java.net.URL
import java.nio.charset.Charset

class URLSource(
    private val url: URL,
    private val charset: Charset = Charsets.UTF_8
) : SourceBase()
{
    constructor(spec: String, charset: Charset = Charsets.UTF_8) : this(
        url = URL(spec),
        charset = charset,
    )

    override val name: String
        get() = this.url.toString()

    override val content: CharSequence by lazy {
        return@lazy this.url.readText(this.charset)
    }
}
