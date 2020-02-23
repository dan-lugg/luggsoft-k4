package com.luggsoft.k4.core

import com.luggsoft.k4.core.io.CharScanner
import com.luggsoft.k4.core.io.DefaultCharScanner

abstract class SourceProviderBase(
    final override val name: String,
    final override val source: CharSequence
) : SourceProvider
{
    final override val sourceCharScanner: CharScanner by lazy {
        return@lazy DefaultCharScanner(this.source)
    }
}
