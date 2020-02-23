package com.luggsoft.k4.core.io

interface CharScanner : Iterator<Char>
{
    val index: Int
    fun read(): Char
    fun peek(): Char
    fun seek(where: Int, seekMode: SeekMode = SeekMode.ABSOLUTE): Int
    operator fun get(index: Int): Char
}
