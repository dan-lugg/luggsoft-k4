package com.luggsoft.k4.core.templates

import com.luggsoft.k4.core.SettingsBase

class TemplateBuilderSettings(
    flagMap: Map<TemplateBuilderFlags, Boolean> = emptyMap(),
) : SettingsBase<TemplateBuilderFlags, TemplateBuilderSettings>(
    flagMap = flagMap,
)
{
    override fun enable(flag: TemplateBuilderFlags): TemplateBuilderSettings = TemplateBuilderSettings(
        flagMap = this.flagMap.withEnabled(flag),
    )

    override fun disable(flag: TemplateBuilderFlags): TemplateBuilderSettings = TemplateBuilderSettings(
        flagMap = this.flagMap.withDisabled(flag),
    )

    companion object
    {
        fun createDefault(): TemplateBuilderSettings = TemplateBuilderSettings()
            .enable(TemplateBuilderFlags.INCLUDE_COMMENTS_INLINE)
            .enable(TemplateBuilderFlags.INCLUDE_COMMENTS_LOGGED)
    }
}
