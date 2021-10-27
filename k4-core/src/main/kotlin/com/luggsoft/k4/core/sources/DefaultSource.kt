package com.luggsoft.k4.core.sources

data class DefaultSource(
    override val name: String,
    override val content: String,
) : Source
