package com.luggsoft.k4.v2.templates

import com.luggsoft.k4.v2.SettingsBase

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
}
