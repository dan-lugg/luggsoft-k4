package com.luggsoft.k4.v2.sources

data class DefaultSource(
    override val name: String,
    override val content: String,
) : Source
