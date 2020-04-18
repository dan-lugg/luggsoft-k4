package com.luggsoft.k4.scaffolding

import com.luggsoft.common.utils.partial

class StaticFileContentNode(
    name: String,
    val content: Content
) : FileContentNodeBase(
    name = name
)

fun foo() {
    String::plus.partial("asdf")
}
