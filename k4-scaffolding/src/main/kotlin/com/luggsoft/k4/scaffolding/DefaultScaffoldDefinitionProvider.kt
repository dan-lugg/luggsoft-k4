package com.luggsoft.k4.scaffolding

import com.luggsoft.common.logger
import java.io.Reader
import java.io.StringReader
import javax.script.ScriptEngine
import javax.script.ScriptEngineManager
import javax.script.SimpleScriptContext

class DefaultScaffoldDefinitionProvider(
    private val scriptReader: Reader,
    private val scriptEngine: ScriptEngine
) : ScaffoldDefinitionProvider
{
    override fun getScaffoldDefinition(): ScaffoldDefinition
    {
        try
        {
            val scriptContext = SimpleScriptContext()
            return when (val result = this.scriptEngine.eval(this.scriptReader, scriptContext))
            {
                is ScaffoldDefinition -> result
                else ->
                {
                    throw Exception("Unexpected result, $result")
                }
            }
        }
        catch (exception: Exception)
        {
            this.logger.error("Failed to get scaffold definition", exception)
            throw exception
        }
    }
}

fun main()
{
    val script = """
        import com.luggsoft.k4.scaffolding.ScaffoldDefinition
        
        run {
            System.out.println("Hello!")
            System.out.println("World!")
            return@run ScaffoldDefinition(
                name = "Example",
                nodes = emptyList()
            )
        }
    """
    val scaffoldDefinitionProvider = DefaultScaffoldDefinitionProvider(
        scriptReader = StringReader(script),
        scriptEngine = ScriptEngineManager().getEngineByName("kotlin")
    )

    val scaffoldDefinition = scaffoldDefinitionProvider.getScaffoldDefinition()
}
