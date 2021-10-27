package com.luggsoft.k4.core.vx.io

import com.luggsoft.common.EMPTY_STRING

object UnknownSource : SourceBase()
{
    override val name: String = "<Unknown>"

    override val content: CharSequence = EMPTY_STRING
}
