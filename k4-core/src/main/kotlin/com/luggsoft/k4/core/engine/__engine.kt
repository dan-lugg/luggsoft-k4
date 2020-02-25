package com.luggsoft.k4.core.engine

import com.luggsoft.k4.core.engine.tokenizers.TokenizerSettings

data class EngineSettings(
    val tokenizerSettings: TokenizerSettings = TokenizerSettings.Default
)
