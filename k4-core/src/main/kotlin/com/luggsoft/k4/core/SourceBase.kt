package com.luggsoft.k4.core

import com.luggsoft.common.normalizeLineSeparators

abstract class SourceBase(
    final override val name: String,
    text: CharSequence
) : Source
{
    init
    {
        assert(name.isNotEmpty()) { "String |name| must not be empty." }
        assert(text.isNotEmpty()) { "String |text| must not be empty." }
    }

    final override val text: CharSequence = text.normalizeLineSeparators()
}
