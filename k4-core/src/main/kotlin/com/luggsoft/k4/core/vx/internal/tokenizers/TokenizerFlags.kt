package com.luggsoft.k4.core.vx.internal.tokenizers

import com.luggsoft.k4.core.vx.FlagsBase

class TokenizerFlags : FlagsBase<TokenizerFlags.Flag, TokenizerFlags>()
{
    enum class Flag
    {
        TRIM_LEADING_RAW_WHITESPACE,
        TRIM_TRAILING_RAW_WHITESPACE,
    }

    companion object
    {
        fun createDefault(): TokenizerFlags = TokenizerFlags()
            .enable(Flag.TRIM_LEADING_RAW_WHITESPACE)
            .disable(Flag.TRIM_TRAILING_RAW_WHITESPACE)
    }
}
