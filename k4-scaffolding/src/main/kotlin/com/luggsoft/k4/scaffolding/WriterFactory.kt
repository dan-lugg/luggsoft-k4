package com.luggsoft.k4.scaffolding

import java.io.File
import java.io.Writer

interface WriterFactory
{
    fun createWriter(file: File): Writer
}
