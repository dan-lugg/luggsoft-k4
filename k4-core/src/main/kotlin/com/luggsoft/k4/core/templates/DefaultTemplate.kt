package com.luggsoft.k4.core.templates

import com.luggsoft.k4.core.sources.Source
import java.io.Writer
import javax.script.Invocable
import javax.script.ScriptEngineManager

class DefaultTemplate(
    val script: String,
    override val source: Source,
    val scriptEngineManager: ScriptEngineManager,
) : Template
{
    override fun execute(model: Any?, templateWriter: Any, templateLogger: Any)
    {
        val scriptEngine = this.scriptEngineManager.getEngineByExtension("kts")
        scriptEngine.eval(this.script)

        val invocable = scriptEngine as Invocable
        invocable.invokeFunction("render", model, templateWriter, templateLogger)
    }

    override fun save(writer: Writer) = writer.write(this.script)
}
