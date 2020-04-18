package com.luggsoft.k4.core

import java.io.File
import java.nio.charset.Charset

/**
 * Represents a named input source.
 *
 */
interface Source
{
    /**
     * The [Source] name.
     *
     */
    val name: String

    /**
     * The [Source] content.
     *
     */
    val text: CharSequence

    companion object
    {
        /**
         * Creates a [Source] from a [File], using the result of [File.getAbsolutePath] as the [Source.name], and the result of [File.readText] as the [Source.text].
         *
         * @param file The [File] from which [Source.text] will be read, and the [Source.name] will be provided.
         * @param charset The [Charset] used to read the [file].
         * @return An instance of [Source]
         */
        @JvmStatic
        fun fromFile(file: File, charset: Charset = Charsets.UTF_8): Source = DefaultSource(
            name = file.absolutePath,
            text = file.readText(charset)
        )

        /**
         * Creates a [Source] from a resource [Stream], using the resource name as the [Source.name], and the resource contents as the [Source.text].
         *
         * @param name
         * @param charset
         * @return
         */
        @JvmStatic
        fun fromResource(name: String, charset: Charset = Charsets.UTF_8): Source = DefaultSource(
            name = name,
            text = this::class.java.getResourceAsStream(name).reader(charset).readText()
        )

        /**
         * TODO
         *
         * @param name
         * @param text
         * @return
         */
        @JvmStatic
        fun fromString(name: String, text: String): Source = DefaultSource(
            name = name,
            text = text
        )
    }
}
