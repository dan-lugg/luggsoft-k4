package com.luggsoft.k4.core.io

import java.util.concurrent.atomic.AtomicInteger

abstract class CharScannerBase(
    val charSequence: CharSequence,
    startIndex: Int = 0
) : CharScanner
{
    private val atomicIndex: AtomicInteger =
        AtomicInteger(startIndex)

    override val index: Int
        get() = this.atomicIndex.get()

    override fun read(): Char
    {
        if (this.atomicIndex.get() >= this.charSequence.length)
        {
            return Char.EOF
        }
        return this.charSequence[this.atomicIndex.getAndIncrement()]
    }

    override fun peek(): Char
    {
        if (this.atomicIndex.get() >= this.charSequence.length)
        {
            return Char.EOF
        }
        return this.charSequence[this.atomicIndex.get()]
    }

    override fun seek(where: Int, seekMode: SeekMode): Int
    {
        val index = when (seekMode)
        {
            SeekMode.RELATIVE -> (this.atomicIndex.get() + where).coerceIn(0, this.charSequence.length)
            SeekMode.ABSOLUTE -> (where).coerceIn(0, this.charSequence.length)
        }
        this.atomicIndex.set(index)
        return index
    }

    override fun get(index: Int): Char = this.charSequence[index]

    override fun hasNext(): Boolean = this.peek() != Char.EOF

    override fun next(): Char = this.read()
}
