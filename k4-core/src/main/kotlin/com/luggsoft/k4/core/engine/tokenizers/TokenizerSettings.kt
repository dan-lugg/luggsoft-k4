package com.luggsoft.k4.core.engine.tokenizers

/**
 * [Tokenizer] behavior settings.
 *
 * @property discardNoteTokens Determines whether [NoteToken]s are discarded by the tokenizer.
 * @property discardEmptyTextTokens Determines whether [TextToken]s with no content are discarded by the tokenizer.
 * @property discardFirstBlankTextToken Determines whether the first [TextToken] in a [Source], containing only whitespace, is discarded by the tokenizer.
 * @property discardTrailingLineSeparators Determines whether line separators trailing [NoteToken]s, [InfoToken]s, and [CodeToken]s are discarded by the tokenizer.
 */
interface TokenizerSettings
{
    val discardNoteTokens: Boolean
    val discardEmptyTextTokens: Boolean
    val discardFirstBlankTextToken: Boolean
    val discardTrailingLineSeparators: Boolean

    /**
     * Default [Tokenizer] behavior settings.
     */
    object Default : TokenizerSettings by object : TokenizerSettings
    {
        override val discardNoteTokens: Boolean = true
        override val discardEmptyTextTokens: Boolean = true
        override val discardFirstBlankTextToken: Boolean = true
        override val discardTrailingLineSeparators: Boolean = true
    }
}
