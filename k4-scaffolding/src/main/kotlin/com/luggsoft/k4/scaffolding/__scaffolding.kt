package com.luggsoft.k4.scaffolding

import com.luggsoft.common.EMPTY_STRING
import com.luggsoft.k4.core.DefaultEngine
import com.luggsoft.k4.core.ModelProvider
import org.intellij.lang.annotations.Language
import org.slf4j.LoggerFactory
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper as YamlMapper

@DslMarker
annotation class NodeBuilderDslMarker

interface Node

@NodeBuilderDslMarker
interface NodeBuilder<T : Node>
{
    fun build(): T
}

fun <T : NodeBuilder<U>, U : Node> T.build(block: T.() -> Unit): U = this
    .also(block)
    .build()

///

fun scaffold(block: ScaffoldNodeBuilder.() -> Unit): ScaffoldNode = ScaffoldNodeBuilder()
    .build(block)

data class ScaffoldNode(
    val name: String,
    val description: String,
    val rootDirectoryContentNode: DirectoryContentNode,
) : Node

class ScaffoldNodeBuilder : NodeBuilder<ScaffoldNode>
{
    lateinit var name: String

    lateinit var description: String

    private lateinit var rootDirectoryContentNode: DirectoryContentNode

    override fun build(): ScaffoldNode = ScaffoldNode(
        name = this.name,
        description = this.description,
        rootDirectoryContentNode = this.rootDirectoryContentNode,
    )

    fun rootDirectory(block: DirectoryContentNodeBuilder.() -> Unit = {}): DirectoryContentNode
    {
        val rootDirectoryContentNodeBuilder = DirectoryContentNodeBuilder(
            name = EMPTY_STRING,
        )
        this.rootDirectoryContentNode = rootDirectoryContentNodeBuilder.build(block)
        return this.rootDirectoryContentNode
    }
}

///

interface ContentNode : Node
{
    val name: String
}

///

data class FileContentNode(
    override val name: String,
    val dataNode: DataNode,
) : ContentNode

class FileContentNodeBuilder(
    private val name: String,
) : NodeBuilder<FileContentNode>
{
    private var dataNode: DataNode? = null

    override fun build(): FileContentNode = FileContentNode(
        name = this.name,
        dataNode = this.dataNode ?: EmptyDataNode,
    )

    fun stringData(data: String): StringDataNode
    {
        val dataNode = StringDataNode(data)
        this.dataNode = dataNode
        return dataNode
    }

    fun binaryData(byteArray: ByteArray): BinaryDataNode
    {
        val dataNode = BinaryDataNode(byteArray)
        this.dataNode = dataNode
        return dataNode
    }

    fun templateData(modelName: String, templateName: String): TemplateDataNode
    {
        val dataNode = TemplateDataNode(modelName, templateName)
        this.dataNode = dataNode
        return dataNode
    }
}

///

data class DirectoryContentNode(
    override val name: String,
    val childContentNodes: List<ContentNode>,
) : ContentNode

class DirectoryContentNodeBuilder(
    private val name: String,
    private var childContentNodes: List<ContentNode> = emptyList(),
) : NodeBuilder<DirectoryContentNode>
{
    override fun build(): DirectoryContentNode = DirectoryContentNode(
        name = this.name,
        childContentNodes = this.childContentNodes,
    )

    fun file(name: String, block: FileContentNodeBuilder.() -> Unit = {}): FileContentNode
    {
        val fileContentNodeBuilder = FileContentNodeBuilder(
            name = name,
        )
        val fileContentNode = fileContentNodeBuilder.build(block)
        this.childContentNodes += fileContentNode
        return fileContentNode
    }

    fun directory(name: String, block: DirectoryContentNodeBuilder.() -> Unit = {}): DirectoryContentNode
    {
        val directoryContentNodeBuilder = DirectoryContentNodeBuilder(
            name = name,
        )
        val directoryContentNode = directoryContentNodeBuilder.build(block)
        this.childContentNodes += directoryContentNode
        return directoryContentNode
    }
}

///

interface DataNode : Node

object EmptyDataNode : DataNode

data class StringDataNode(
    val data: String,
) : DataNode

data class BinaryDataNode(
    val data: ByteArray,
) : DataNode

data class TemplateDataNode(
    val modelName: String,
    val templateName: String,
) : DataNode

///

interface NodeVisitor
{
    fun visitNode(nodeList: NodeList)
}

fun NodeVisitor.visitNode(node: Node) = this.visitNode(
    nodeList = NodeList(node),
)

class DefaultNodeVisitor(
    private val modelProvider: ModelProvider,
    private val templateRegistry: TemplateRegistry,
) : NodeVisitor
{
    private val writer = System.out.writer()

    private val logger = LoggerFactory.getLogger("test")

    override fun visitNode(nodeList: NodeList)
    {
        when (val currentNode = nodeList.currentNode)
        {
            is ScaffoldNode ->
            {
                println("${currentNode.name}, ${currentNode.description}")
                this.visitNode(
                    nodeList = nodeList + currentNode.rootDirectoryContentNode,
                )
            }
            is FileContentNode ->
            {
                println("F: ${nodeList.contentNodesPath}")
                when (val dataNode = currentNode.dataNode)
                {
                    is StringDataNode ->
                    {
                    }
                    is BinaryDataNode ->
                    {
                    }
                    is TemplateDataNode ->
                    {
                        val template = this.templateRegistry.getTemplate(
                            name = dataNode.templateName
                        )
                        val model = this.modelProvider.getModel(
                            name = dataNode.modelName,
                            kClass = template.modelKClass,
                        )
                        template.execute(
                            model = model,
                            writer = this.writer,
                            logger = this.logger,
                        )
                        this.writer.flush()
                    }
                }
            }
            is DirectoryContentNode ->
            {
                println("D: ${nodeList.contentNodesPath}")

                for (childContentNode in currentNode.childContentNodes)
                {
                    this.visitNode(
                        nodeList = nodeList + childContentNode
                    )
                }
            }

        }
    }
}

data class NodeList(
    val nodes: List<Node>,
) : Iterable<Node>
{
    constructor(vararg nodes: Node) : this(
        nodes = nodes.toList(),
    )

    val currentNode: Node
        get() = this.nodes.last()

    val contentNodes: List<ContentNode>
        get() = this.nodes.filterIsInstance<ContentNode>()

    val contentNodesPath: String
        get() = this.contentNodes.joinToString(
            separator = "/",
            transform = ContentNode::name,
        )

    override fun iterator(): Iterator<Node> = this.nodes.iterator()

    operator fun plus(node: Node) = NodeList(this.nodes + node)
}

///

///

data class FooModel(
    val count: Int,
    val packageName: String,
    val applicationName: String,
)

data class BarModel(
    val title: String,
)

///

@Language("yaml")
val templatesYaml = """
-
  name: foo
  code: |
    "Hello world!" -- <#= model #>
-  
  name: bar
  code: |
    "Goodbye universe!" -- <#= model #> 
"""

data class TemplateDefinition(
    val name: String,
    val code: String,
)

fun main()
{
    val templateRegistry = DefaultTemplateRegistry.fromYaml(
        yaml = templatesYaml,
        yamlMapper = YamlMapper.builder()
            .findAndAddModules()
            .build(),
        engine = DefaultEngine.Instance,
    )

    for ((name, template) in templateRegistry)
    {
        println("$name -> ${template.script}")
    }

    /*
    val scaffoldNode = scaffold {
        name = "example-name"
        description = "This is an example description"

        rootDirectory {
            file(name = "README.md"){
                templateData(
                    modelName = "bar",
                    templateName = "test/README.md.k4"
                )
            }
            directory("src") {
                file("script2.kts") {
                    templateData(
                        modelName = "foo",
                        templateName = "Application.kt.k4"
                    )
                }
            }
        }
    }

    val modelProvider = DefaultModelProvider(
        modelNameMap = mapOf(
            "foo" to FooModel(
                count = 7,
                packageName = "noodles",
                applicationName = "AlsoNoodles",
            ),
            "bar" to BarModel(
                title = "Hello World!",
            ),
        ),
    )

    val directoryName = "/Users/dan.lugg/IdeaProjects/kt-k4/k4-scaffolding/src/main/resources"
    val templateRegistry = DefaultTemplateRegistry.createFromDirectory(
        directoryName = directoryName,
        engine = DefaultEngine.Instance,
    )

    val nodeVisitor = DefaultNodeVisitor(
        modelProvider = modelProvider,
        templateRegistry = templateRegistry,
    )

    nodeVisitor.visitNode(scaffoldNode)
    */
}
