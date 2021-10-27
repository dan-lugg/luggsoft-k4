package com.luggsoft.k4.v2.sources

import com.luggsoft.k4.v2.SettingsBase

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
}
