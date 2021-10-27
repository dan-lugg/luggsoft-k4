package com.luggsoft.k4.core.sources

import com.luggsoft.k4.core.SettingsBase

class SourceParserSettings(
    flagMap: Map<SourceParserFlags, Boolean> = emptyMap(),
) : SettingsBase<SourceParserFlags, SourceParserSettings>(
    flagMap = flagMap,
)
{
    override fun enable(flag: SourceParserFlags): SourceParserSettings = SourceParserSettings(
        flagMap = this.flagMap.withEnabled(flag),
    )

    override fun disable(flag: SourceParserFlags): SourceParserSettings = SourceParserSettings(
        flagMap = this.flagMap.withDisabled(flag),
    )

    companion object
    {
        fun createDefault(): SourceParserSettings = SourceParserSettings()
            .enable(SourceParserFlags.SKIP_TAG_TRAILING_NEWLINES)
    }
}
