package com.luggsoft.k4.core.templates

import com.luggsoft.k4.core.SettingsBase

class TemplateGeneratorSettings(
    flagMap: Map<TemplateGeneratorFlags, Boolean> = emptyMap(),
) : SettingsBase<TemplateGeneratorFlags, TemplateGeneratorSettings>(
    flagMap = flagMap,
)
{
    override fun enable(flag: TemplateGeneratorFlags): TemplateGeneratorSettings = TemplateGeneratorSettings(
        flagMap = this.flagMap.withEnabled(flag),
    )

    override fun disable(flag: TemplateGeneratorFlags): TemplateGeneratorSettings = TemplateGeneratorSettings(
        flagMap = this.flagMap.withDisabled(flag),
    )

    companion object
    {
        fun createDefault(): TemplateGeneratorSettings = TemplateGeneratorSettings()
            .enable(TemplateGeneratorFlags.INCLUDE_COMMENTS_INLINE)
            .enable(TemplateGeneratorFlags.INCLUDE_COMMENTS_LOGGED)
    }
}
