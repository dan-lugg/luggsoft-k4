package com.luggsoft.k4.intellij

import com.intellij.lang.Language
import com.intellij.openapi.fileTypes.LanguageFileType
import com.intellij.openapi.fileTypes.StdFileTypes
import com.intellij.psi.templateLanguages.TemplateLanguage

object K4Language : Language("k4", "text/k4"), TemplateLanguage
{
    val defaultTemplateLanguageFileType: LanguageFileType = StdFileTypes.PLAIN_TEXT
}
