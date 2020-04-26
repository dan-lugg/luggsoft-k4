package com.luggsoft.k4.scaffolding.templates

import com.luggsoft.k4.core.Source
import com.luggsoft.k4.core.engine.Engine
import com.luggsoft.k4.scaffolding.ScaffoldDefinitionProvider
import com.luggsoft.k4.scaffolding.internals.OBJECT_WRITER
import java.io.File
import java.io.InputStream
import java.io.Writer

interface Content
{
    val name: String

    fun getInputStream(): InputStream
}

interface ContentProvider
{
    fun getContent(name: String): Content
}

class FileContentProvider(
    private val baseDirectory: File
) : ContentProvider
{
    override fun getContent(name: String): Content
    {
        return FileContent(
            file = File(this.baseDirectory, name)
        )
    }
}

class FileContent(
    private val file: File
) : Content
{
    override val name: String
        get()
        {
            return this.file.path
        }

    override fun getInputStream(): InputStream
    {
        return this.file.inputStream()
    }
}

class MemoryContentProvider(
    private val memoryContents: List<MemoryContent>
) : ContentProvider
{
    override fun getContent(name: String): Content
    {
        return this.memoryContents.first { memoryContent ->
            return@first memoryContent.name == name
        }
    }
}

class MemoryContent(
    override val name: String,
    private val text: String
) : Content
{
    override fun getInputStream(): InputStream
    {
        return this.text.byteInputStream()
    }
}

////

interface TemplateRenderer
{
    fun renderTemplate(name: String, model: Any?, writer: Writer)
}

class K4TemplateRenderer(
    private val engine: Engine,
    private val contentProvider: ContentProvider
) : TemplateRenderer
{
    override fun renderTemplate(name: String, model: Any?, writer: Writer)
    {
        val content = this.contentProvider.getContent(name)
        val sourceReader = content.getInputStream().reader()
        val source = Source.fromReader(name, sourceReader)
        return this.engine.compileAndExecute(source, writer, model)
    }
}

////

class ScaffoldingEngine(
    val contentProvider: ContentProvider,
    val scaffoldDefinitionProvider: ScaffoldDefinitionProvider
)
{
    fun execute()
    {
        val scaffoldDefinition = this.scaffoldDefinitionProvider.getScaffoldDefinition()

    }
}

////

interface ContentManager
{
    fun getContent(name: String): Content
}

class FileContentManager(
    private val namePathMapping: Map<String, File>
) : ContentManager
{
    init
    {
        println(this.namePathMapping)
    }

    constructor(baseDirectory: File) : this(
        namePathMapping = FileContentManager.buildNamePathMapping(baseDirectory)
    )

    override fun getContent(name: String): Content
    {
        return FileContent(
            file = this.namePathMapping.getValue(name)
        )
    }

    companion object
    {
        fun buildNamePathMapping(baseDirectory: File): Map<String, File>
        {
            fun recursiveBuildNamePathMapping(directory: File): Map<String, File>
            {
                return directory.listFiles()
                    .orEmpty()
                    .flatMap { file ->
                        return@flatMap when
                        {
                            file.isFile -> listOf(file.toRelativeString(baseDirectory) to file)
                            file.isDirectory -> recursiveBuildNamePathMapping(file).toList()
                            else -> TODO()
                        }
                    }
                    .toMap()
            }

            return recursiveBuildNamePathMapping(baseDirectory)
        }
    }
}

fun File.listFilesMappingRecursive(): Map<String, File>
{
    fun buildRecursiveMapping(directory: File): Map<String, File>
    {
        return directory.listFiles()
            .orEmpty()
            .flatMap { file ->
                return@flatMap when
                {
                    file.isFile -> listOf(file.toRelativeString(this) to file)
                    file.isDirectory -> buildRecursiveMapping(file).toList()
                    else -> TODO()
                }
            }
            .toMap()
    }

    return buildRecursiveMapping(this)
}

fun main()
{
    val contentManager = FileContentManager(
        baseDirectory = File("c:/temp/k4")
    )

    val content = contentManager.getContent("/example/foo.txt")

    val result = object
    {
        val name = content.name
        val text = content.getInputStream().reader().readText()
    }

    OBJECT_WRITER.writeValueAsString(result).also(::println)
}
