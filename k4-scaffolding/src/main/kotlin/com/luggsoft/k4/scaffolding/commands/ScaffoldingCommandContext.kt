package com.luggsoft.k4.scaffolding.commands

import com.luggsoft.common.commands.CommandContext
import java.io.File

class ScaffoldingCommandContext(
    private val rootPath: String
) : CommandContext
{
    val rootDirectory: File
        get() = File(this.rootPath)
}
