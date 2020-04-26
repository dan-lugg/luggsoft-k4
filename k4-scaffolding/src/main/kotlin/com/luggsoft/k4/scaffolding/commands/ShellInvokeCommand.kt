package com.luggsoft.k4.scaffolding.commands

import com.luggsoft.common.commands.Command

class ShellInvokeCommand(
    val parts: List<String>
) : Command<Int>
