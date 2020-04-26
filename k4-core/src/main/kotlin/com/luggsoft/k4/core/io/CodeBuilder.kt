package com.luggsoft.k4.core.io

class CodeBuilder(
    private val stringBuffer: StringBuffer = StringBuffer(),
    indentLevel: Int = 0,
    private val indentString: String = "\t",
    private val lineSeparator: String = System.lineSeparator()
) : Appendable, CharSequence
{
    private val indentLevel: Int = indentLevel.coerceAtLeast(0)

    override val length: Int
        get()
        {
            return this.stringBuffer.length
        }

    override fun toString(): String
    {
        return this.stringBuffer.toString()
    }

    override fun append(charSequence: CharSequence?): CodeBuilder
    {
        if (this.stringBuffer.endsWith(this.lineSeparator) && this.indentLevel > 0)
        {
            val indent = this.indentString.repeat(this.indentLevel)
            this.stringBuffer.append(indent)
        }

        this.stringBuffer.append(charSequence)
        return this
    }

    override fun append(charSequence: CharSequence?, start: Int, end: Int): CodeBuilder
    {
        if (this.stringBuffer.endsWith(this.lineSeparator) && this.indentLevel > 0)
        {
            val indent = this.indentString.repeat(this.indentLevel)
            this.stringBuffer.append(indent)
        }

        this.stringBuffer.append(charSequence, start, end)
        return this
    }

    override fun append(char: Char): CodeBuilder
    {
        if (this.stringBuffer.endsWith(this.lineSeparator) && this.indentLevel > 0)
        {
            val indent = this.indentString.repeat(this.indentLevel)
            this.stringBuffer.append(indent)
        }
        
        this.stringBuffer.append(char)
        return this
    }

    override fun get(index: Int): Char
    {
        return this.stringBuffer.get(index)
    }

    override fun subSequence(startIndex: Int, endIndex: Int): CharSequence
    {
        return this.stringBuffer.subSequence(startIndex, endIndex)
    }

    fun appendln(): CodeBuilder
    {
        return this.append(this.lineSeparator)
    }

    fun appendln(charSequence: CharSequence?): CodeBuilder
    {
        return this.append(charSequence).append(this.lineSeparator)
    }

    fun appendln(charSequence: CharSequence?, start: Int, end: Int): CodeBuilder
    {
        return this.append(charSequence, start, end).append(this.lineSeparator)
    }

    fun append(value: Any?): CodeBuilder
    {
        return when (value)
        {
            null -> this
            else -> value.toString().let(this::append)
        }
    }

    fun appendln(value: Any?): CodeBuilder
    {
        return when (value)
        {
            null -> this
            else -> value.toString().let(this::appendln)
        }
    }

    fun indent(): CodeBuilder
    {
        return CodeBuilder(
            stringBuffer = this.stringBuffer,
            indentLevel = this.indentLevel + 1,
            lineSeparator = this.lineSeparator
        )
    }

    fun dedent(): CodeBuilder
    {
        return CodeBuilder(
            stringBuffer = this.stringBuffer,
            indentLevel = this.indentLevel - 1,
            lineSeparator = this.lineSeparator
        )
    }

    fun indented(block: (CodeBuilder) -> Unit): CodeBuilder
    {
        return this.indent().also(block)
    }

    fun dedented(block: (CodeBuilder) -> Unit): CodeBuilder
    {
        return this.dedent().also(block)
    }
}
