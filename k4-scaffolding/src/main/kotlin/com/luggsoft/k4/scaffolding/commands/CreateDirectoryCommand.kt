package com.luggsoft.k4.scaffolding.commands

import com.luggsoft.common.commands.Command
import java.io.File

class CreateDirectoryCommand(
    val path: String
) : Command<File>
