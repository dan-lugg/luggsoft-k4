package com.luggsoft.k4.intellij

import com.intellij.openapi.fileTypes.LanguageFileType
import javax.swing.Icon
import javax.swing.ImageIcon

class K4LanguageFileType() : LanguageFileType(K4Language)
{
    override fun getDefaultExtension(): String
    {
        return "k4"
    }

    override fun getName(): String
    {
        return "K4"
    }

    override fun getDescription(): String
    {
        return "Kotlin Text Template Transformation Tools"
    }

    override fun getIcon(): Icon
    {
        val imageData = this.getIconImageData()
        return ImageIcon(imageData)
    }

    private fun getIconImageData(): ByteArray
    {
        return this::class.java.getResourceAsStream("./icon.png")?.readBytes()
            ?: TODO()
    }
}
