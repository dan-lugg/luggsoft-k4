package com.luggsoft.k4.core.templates

import java.io.File
import java.io.OutputStream
import java.nio.charset.Charset

fun Template.save(outputStream: OutputStream, charset: Charset = Charsets.UTF_8) = this.save(
    writer = outputStream.writer(charset)
)

fun Template.save(file: File, charset: Charset = Charsets.UTF_8) = this.save(
    writer = file.writer(charset)
)
