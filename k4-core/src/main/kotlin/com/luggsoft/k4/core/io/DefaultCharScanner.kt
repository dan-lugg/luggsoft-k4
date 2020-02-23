package com.luggsoft.k4.core.io

import java.io.InputStream
import java.io.Reader
import java.nio.charset.Charset

internal class DefaultCharScanner(
    charSequence: CharSequence
) : CharScannerBase(
    charSequence = charSequence
)
{
    constructor(reader: Reader) : this(
        charSequence = reader.readText()
    )

    constructor(inputStream: InputStream, charset: Charset = Charsets.UTF_8) : this(
        reader = inputStream.reader(charset)
    )
}
