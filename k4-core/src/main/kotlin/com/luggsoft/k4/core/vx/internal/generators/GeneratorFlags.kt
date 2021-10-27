package com.luggsoft.k4.core.vx.internal.generators

import com.luggsoft.k4.core.vx.FlagsBase

class GeneratorFlags : FlagsBase<GeneratorFlags.Flag, GeneratorFlags>()
{
    enum class Flag
    {
        LOG_COMMENTS,
        INCLUDE_COMMENTS,
        INCLUDE_SOURCE_MARKERS,
    }

    companion object
    {
        fun createDefault(): GeneratorFlags = GeneratorFlags()
    }
}
