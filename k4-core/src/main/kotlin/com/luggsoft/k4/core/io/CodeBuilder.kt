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
        get() = this.stringBuffer.length

    override fun toString(): String = this.stringBuffer.toString()

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

    override fun get(index: Int): Char = this.stringBuffer
        .get(index)

    override fun subSequence(startIndex: Int, endIndex: Int): CharSequence = this.stringBuffer
        .subSequence(startIndex, endIndex)

    fun appendln(): CodeBuilder = this
        .append(this.lineSeparator)

    fun appendln(charSequence: CharSequence?): CodeBuilder = this
        .append(charSequence)
        .append(this.lineSeparator)

    fun appendln(charSequence: CharSequence?, start: Int, end: Int): CodeBuilder = this
        .append(charSequence, start, end)
        .append(this.lineSeparator)

    fun append(value: Any?): CodeBuilder = when (value)
    {
        null -> this
        else -> value.toString().let(this::append)
    }

    fun appendln(value: Any?): CodeBuilder = when (value)
    {
        null -> this
        else -> value.toString().let(this::appendln)
    }

    fun indent(): CodeBuilder = CodeBuilder(
        stringBuffer = this.stringBuffer,
        indentLevel = this.indentLevel + 1,
        lineSeparator = this.lineSeparator
    )

    fun dedent(): CodeBuilder = CodeBuilder(
        stringBuffer = this.stringBuffer,
        indentLevel = this.indentLevel - 1,
        lineSeparator = this.lineSeparator
    )

    fun indented(block: (CodeBuilder) -> Unit): CodeBuilder
    {
        this.indent().also(block)
        return this
    }

    fun dedented(block: (CodeBuilder) -> Unit): CodeBuilder
    {
        this.dedent().also(block)
        return this
    }
}
