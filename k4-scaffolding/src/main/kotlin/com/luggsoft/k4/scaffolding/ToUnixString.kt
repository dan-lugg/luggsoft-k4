package com.luggsoft.k4.scaffolding

import java.nio.file.Path

fun Path.toUnixString(): String = this.joinToString(separator = "/")
