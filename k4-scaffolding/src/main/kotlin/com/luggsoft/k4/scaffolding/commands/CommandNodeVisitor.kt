package com.luggsoft.k4.scaffolding.commands

import com.luggsoft.common.commands.Command
import com.luggsoft.k4.core.Source
import com.luggsoft.k4.core.Source.Companion
import com.luggsoft.k4.core.engine.Engine
import com.luggsoft.k4.scaffolding.nodes.DirectoryNode
import com.luggsoft.k4.scaffolding.nodes.Node
import com.luggsoft.k4.scaffolding.nodes.NodeVisitorBase
import com.luggsoft.k4.scaffolding.nodes.StaticFileNode
import com.luggsoft.k4.scaffolding.nodes.TemplateFileNode
import java.io.ByteArrayOutputStream
import java.io.StringWriter

class CommandNodeVisitor(
    private val engine: Engine
) : NodeVisitorBase<List<Command<*>>>()
{
    override fun visitDirectoryNode(directoryNode: DirectoryNode, ancestorNodes: List<Node>): List<Command<*>>
    {
        val commandSequence = sequence {
            val createDirectoryCommand = CreateDirectoryCommand(
                path = this@CommandNodeVisitor.createPath(ancestorNodes + directoryNode)
            )

            this@sequence.yield(createDirectoryCommand)
            for (childNode in directoryNode.childNodes)
            {
                val childCommands = this@CommandNodeVisitor.visitNode(childNode, ancestorNodes + directoryNode)
                this@sequence.yieldAll(childCommands)
            }
        }

        return commandSequence.toList()
    }

    override fun visitStaticFileNode(staticFileNode: StaticFileNode, ancestorNodes: List<Node>): List<Command<*>>
    {
        val createFileCommand = CreateFileCommand(
            path = this.createPath(ancestorNodes + staticFileNode),
            contentBytesProvider = object : ContentBytesProvider
            {
                override fun getContentBytes(): ByteArray
                {
                    return ByteArrayOutputStream().use { byteArrayOutputStream ->
                        staticFileNode.staticContent.write(byteArrayOutputStream)
                        byteArrayOutputStream.flush()
                        return@use byteArrayOutputStream.toByteArray()
                    }
                }
            }
        )

        return listOf(createFileCommand)
    }

    override fun visitTemplateFileNode(templateFileNode: TemplateFileNode, ancestorNodes: List<Node>): List<Command<*>>
    {
        val createFileCommand = CreateFileCommand(
            path = this.createPath(ancestorNodes + templateFileNode),
            contentBytesProvider = object : ContentBytesProvider
            {
                override fun getContentBytes(): ByteArray
                {
                    val writer = StringWriter()
                    this@CommandNodeVisitor.engine.compileAndExecute(
                        source = Source.fromString(templateFileNode.name, templateFileNode.template),
                        writer = writer,
                        model = templateFileNode.model
                    )

                    return writer.toString().toByteArray()
                }
            }
        )

        return listOf(createFileCommand)
    }

    private fun createPath(nodes: List<Node>): String
    {
        return nodes.joinToString(separator = "/", transform = Node::name)
    }
}
