package com.luggsoft.k4.scaffolding.commands

import com.luggsoft.common.commands.Command
import java.io.File

class CreateFileCommand(
    val path: String,
    val contentBytesProvider: ContentBytesProvider
) : Command<File>
