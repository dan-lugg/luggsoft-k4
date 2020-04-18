package com.luggsoft.k4.scaffolding

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.luggsoft.common.commands.Command
import com.luggsoft.common.commands.CommandHandler
import com.luggsoft.common.commands.CommandInvoker
import com.luggsoft.common.commands.DefaultCommandHandlerProvider
import com.luggsoft.common.commands.DefaultCommandInvoker
import com.luggsoft.common.commands.commandHandlerMappingOf
import com.luggsoft.k4.core.Source
import com.luggsoft.k4.core.engine.DefaultEngine
import com.luggsoft.k4.core.engine.Engine
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.StringWriter

interface ContentBytesProvider
{
    fun getContentBytes(): ByteArray
}

class CreateFileCommand(
    val path: String,
    val contentBytesProvider: ContentBytesProvider
) : Command<File>

class CreateFileCommandHandler : CommandHandler<CreateFileCommand, File>
{
    override fun handleCommand(command: CreateFileCommand): File
    {
        /*
        try
        {
            val file = File(command.path)
            file.setReadable(true)
            file.setWritable(true)
            val bytes = command.contentBytesProvider.getContentBytes()
            file.writeBytes(bytes)
            return file
        }
        catch (exception: Exception)
        {
            ("Failed to create file at path, ${command.path}").also { message ->
                this.logger.error(message, exception)
                throw Exception(message, exception)
            }
        }
        */

        val content = String(command.contentBytesProvider.getContentBytes())
        println("F: ${command.path} = ${jacksonObjectMapper().writeValueAsString(content)}")
        return File(command.path)
    }
}

class CreateDirectoryCommand(
    val path: String
) : Command<File>

class CreateDirectoryCommandHandler : CommandHandler<CreateDirectoryCommand, File>
{
    override fun handleCommand(command: CreateDirectoryCommand): File
    {
        /*
        val directory = File(command.path)
        if (directory.mkdirs())
        {
            return directory
        }

        throw Exception("Failed to create directory at path, ${command.path}")
        */
        println("D: ${command.path}")
        return File(command.path)
    }
}

class CommandContentNodeVisitor(
    private val engine: Engine
) : ContentNodeVisitorBase<List<Command<*>>>()
{

    override fun visitDirectoryContentNode(directoryContentNode: DirectoryContentNode, ancestorContentNodes: List<ContentNode>): List<Command<*>>
    {
        val commandSequence = sequence {
            val createDirectoryCommand = CreateDirectoryCommand(
                path = this@CommandContentNodeVisitor.createPath(ancestorContentNodes + directoryContentNode)
            )

            this@sequence.yield(createDirectoryCommand)
            for (childContentNode in directoryContentNode.childContentNodes)
            {
                val childCommands = this@CommandContentNodeVisitor.visitContentNode(childContentNode, ancestorContentNodes + directoryContentNode)
                this@sequence.yieldAll(childCommands)
            }
        }

        return commandSequence.toList()
    }

    override fun visitStaticFileContentNode(staticFileContentNode: StaticFileContentNode, ancestorContentNodes: List<ContentNode>): List<Command<*>>
    {
        val createFileCommand = CreateFileCommand(
            path = this.createPath(ancestorContentNodes + staticFileContentNode),
            contentBytesProvider = object : ContentBytesProvider
            {
                override fun getContentBytes(): ByteArray
                {
                    return ByteArrayOutputStream().use { byteArrayOutputStream ->
                        staticFileContentNode.content.write(byteArrayOutputStream)
                        byteArrayOutputStream.flush()
                        return@use byteArrayOutputStream.toByteArray()
                    }
                }
            }
        )

        return listOf(createFileCommand)
    }

    override fun visitTemplateFileContentNode(templateFileContentNode: TemplateFileContentNode, ancestorContentNodes: List<ContentNode>): List<Command<*>>
    {
        val createFileCommand = CreateFileCommand(
            path = this.createPath(ancestorContentNodes + templateFileContentNode),
            contentBytesProvider = object : ContentBytesProvider
            {
                override fun getContentBytes(): ByteArray
                {
                    val writer = StringWriter()
                    this@CommandContentNodeVisitor.engine.compileAndExecute(
                        source = Source.fromString(templateFileContentNode.name, templateFileContentNode.template),
                        writer = writer,
                        model = templateFileContentNode.model
                    )

                    return writer.toString().toByteArray()
                }
            }
        )

        return listOf(createFileCommand)
    }

    private fun createPath(contentNodes: List<ContentNode>): String
    {
        return contentNodes.joinToString(separator = "/", transform = ContentNode::name)
    }
}

///

fun process(
    contentNode: ContentNode,
    commandInvoker: CommandInvoker,
    commandContentNodeVisitor: CommandContentNodeVisitor
)
{
    commandContentNodeVisitor.visitContentNode(contentNode).forEach { command ->
        val result = commandInvoker.invokeCommand(command)
        println("R: $result")
        println()
    }
}

fun getContentNode(): ContentNode
{
    return rootDirectory {
        addStaticFile("foo.txt") {
            withText(
                text = trimIndent(
                    string = """
                        Hello world from foo!
                    """
                )
            )
        }

        addStaticFile("bar.txt") {
            withText(
                text = trimIndent(
                    string = """
                        Hello world from bar!
                    """
                )
            )
        }

        addDirectory("src") {
            (1..3).forEach { number ->
                addStaticFile("file$number.txt") {
                    withText(
                        text = trimIndent(
                            string = """
                                Hello world from number $number!
                            """
                        )
                    )
                }

                addDirectory("directory$number") {
                    addStaticFile("foo$number.txt") {
                        withText(
                            text = trimIndent(
                                string = """
                                    Hello world from foo $number!
                                """
                            )
                        )
                    }

                    addTemplateFile("bar$number.txt") {
                        withModel("SOMETHING$number")
                        withTemplate(
                            code = trimIndent(
                                string = """
                                    <#@
                                        modelClassName: java.lang.String
                                    #>
                                    Hello world from model = <#= model #>!
                                    <#! (1..3).forEach { index -> #>
                                        Saying hello also from <#= index #> for <#= model #>!
                                    <#! } #>
                                """
                            )
                        )
                    }
                }
            }
        }
    }
}

fun trimIndent(string: String) = string.trimIndent()

fun main()
{
    val contentNode = getContentNode()

    val commandInvoker = DefaultCommandInvoker(
        commandHandlerProvider = DefaultCommandHandlerProvider(
            mappings = listOf(
                commandHandlerMappingOf(::CreateFileCommandHandler),
                commandHandlerMappingOf(::CreateDirectoryCommandHandler)
            )
        )
    )

    val commandContentNodeVisitor = CommandContentNodeVisitor(
        engine = DefaultEngine.Instance
    )

    process(contentNode, commandInvoker, commandContentNodeVisitor)
}
