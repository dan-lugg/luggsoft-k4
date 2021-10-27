package com.luggsoft.k4.cli.helpers

class StdoutTarget : Target
{
    override fun write(charSequence: CharSequence) = print(charSequence)
}
