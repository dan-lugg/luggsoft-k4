package com.luggsoft.k4.core

import com.luggsoft.k4.core.sources.SourceIterator

fun SourceIterator.tryConsumeNextDelimited(delimiter: CharSequence, stringBuilder: StringBuilder): Boolean
{
    if (this.peekEquals(delimiter))
    {
        stringBuilder.append(this.next(delimiter.length))

        while (!this.peekEquals(delimiter))
        {
            stringBuilder.append(this.next())
        }

        stringBuilder.append(this.next(delimiter.length))
        return true
    }

    return false
}
