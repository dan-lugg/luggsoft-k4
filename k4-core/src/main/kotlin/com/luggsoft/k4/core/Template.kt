package com.luggsoft.k4.core

interface Template
{
    fun render(model: Any? = null): String
}
