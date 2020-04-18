package com.luggsoft.k4.core

import com.luggsoft.common.normalizeLineSeparators

class DefaultSource(
    override val name: String,
    text: CharSequence
) : Source
{
    init
    {
        assert(name.isNotEmpty()) { "String |name| must not be empty." }
        assert(text.isNotEmpty()) { "String |text| must not be empty." }
    }

    override val text: CharSequence = text.normalizeLineSeparators()
}

