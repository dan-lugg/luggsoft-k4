package com.luggsoft.k4.core.templates

import com.luggsoft.k4.core.sources.Source
import org.slf4j.Logger
import java.io.Writer
import javax.script.Invocable
import javax.script.ScriptEngine
import javax.script.ScriptEngineManager
import kotlin.reflect.KClass

class DefaultTemplate<T : Any>(
    override val source: Source,
    override val script: String,
    override val modelKClass: KClass<T>,
    val scriptEngineManager: ScriptEngineManager,
) : Template<T>
{
    override fun execute(model: T, writer: Writer, logger: Logger)
    {
        if (!this.modelKClass.isInstance(model))
        {
            TODO("Expected ${this.modelKClass}, received ${model::class}")
        }

        lateinit var invocable: Invocable
        lateinit var scriptEngine: ScriptEngine

        try
        {
            scriptEngine = this.scriptEngineManager.getEngineByExtension("kts")
            scriptEngine.eval(this.script)
            invocable = scriptEngine as Invocable
            invocable.invokeFunction("render", model, writer, logger)
        }
        catch (exception: NoSuchMethodException)
        {
            throw exception
        }
        catch (exception: Exception)
        {
            throw exception
        }
    }

    override fun writeScript(writer: Writer) = writer.write(this.script)

    private interface ScriptWrapper
    {
        fun render(model: Any, writer: Writer, logger: Logger)
    }
}
