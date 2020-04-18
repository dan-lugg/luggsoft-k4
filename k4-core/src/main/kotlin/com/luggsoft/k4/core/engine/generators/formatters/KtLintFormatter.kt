package com.luggsoft.k4.core.engine.generators.formatters

import com.luggsoft.common.logger
import com.pinterest.ktlint.core.KtLint
import com.pinterest.ktlint.core.KtLint.Params
import com.pinterest.ktlint.core.LintError
import com.pinterest.ktlint.core.RuleSet
import com.pinterest.ktlint.ruleset.experimental.ExperimentalRuleSetProvider
import com.pinterest.ktlint.ruleset.standard.ChainWrappingRule
import com.pinterest.ktlint.ruleset.standard.CommentSpacingRule
import com.pinterest.ktlint.ruleset.standard.FilenameRule
import com.pinterest.ktlint.ruleset.standard.FinalNewlineRule
import com.pinterest.ktlint.ruleset.standard.ImportOrderingRule
import com.pinterest.ktlint.ruleset.standard.IndentationRule
import com.pinterest.ktlint.ruleset.standard.ModifierOrderRule
import com.pinterest.ktlint.ruleset.standard.NoBlankLineBeforeRbraceRule
import com.pinterest.ktlint.ruleset.standard.NoConsecutiveBlankLinesRule
import com.pinterest.ktlint.ruleset.standard.NoEmptyClassBodyRule
import com.pinterest.ktlint.ruleset.standard.NoLineBreakAfterElseRule
import com.pinterest.ktlint.ruleset.standard.NoLineBreakBeforeAssignmentRule
import com.pinterest.ktlint.ruleset.standard.NoMultipleSpacesRule
import com.pinterest.ktlint.ruleset.standard.NoSemicolonsRule
import com.pinterest.ktlint.ruleset.standard.NoTrailingSpacesRule
import com.pinterest.ktlint.ruleset.standard.NoUnitReturnRule
import com.pinterest.ktlint.ruleset.standard.NoUnusedImportsRule
import com.pinterest.ktlint.ruleset.standard.NoWildcardImportsRule
import com.pinterest.ktlint.ruleset.standard.ParameterListWrappingRule
import com.pinterest.ktlint.ruleset.standard.SpacingAroundColonRule
import com.pinterest.ktlint.ruleset.standard.SpacingAroundCommaRule
import com.pinterest.ktlint.ruleset.standard.SpacingAroundCurlyRule
import com.pinterest.ktlint.ruleset.standard.SpacingAroundDotRule
import com.pinterest.ktlint.ruleset.standard.SpacingAroundKeywordRule
import com.pinterest.ktlint.ruleset.standard.SpacingAroundOperatorsRule
import com.pinterest.ktlint.ruleset.standard.SpacingAroundParensRule
import com.pinterest.ktlint.ruleset.standard.SpacingAroundRangeOperatorRule
import com.pinterest.ktlint.ruleset.standard.StringTemplateRule

class KtLintFormatter(
    private val ruleSets: List<RuleSet> = Companion.ruleSets,
    private val script: Boolean = true,
    private val debug: Boolean = true
) : Formatter
{
    override fun format(input: String): String
    {
        try
        {
            val params = this.getParams(input)
            return KtLint.format(params)
        }
        catch (exception: Exception)
        {
            ("Failed to format input").also { message ->
                this.logger.error(message, exception)
                throw FormatterException(message, exception)
            }
        }
    }

    private fun getParams(input: String): Params
    {
        return Params(
            text = input,
            ruleSets = this.ruleSets,
            debug = this.debug,
            script = this.script,
            cb = { lintError: LintError, corrected: Boolean ->
                this.logger.debug("KtLint error, ${lintError.ruleId} ${lintError.detail} @ [${lintError.line}:${lintError.col}")
            }
        )
    }

    private companion object
    {
        val ruleSets: List<RuleSet>
            get()
            {
                return listOf(
                    // StandardRuleSetProvider().get(),
                    ExperimentalRuleSetProvider().get(),
                    RuleSet(
                        "custom-standard",
                        ChainWrappingRule(),
                        CommentSpacingRule(),
                        FilenameRule(),
                        FinalNewlineRule(),
                        ImportOrderingRule(),
                        IndentationRule(),
                        // MaxLineLengthRule(),
                        ModifierOrderRule(),
                        NoBlankLineBeforeRbraceRule(),
                        NoConsecutiveBlankLinesRule(),
                        NoEmptyClassBodyRule(),
                        NoLineBreakAfterElseRule(),
                        NoLineBreakBeforeAssignmentRule(),
                        NoMultipleSpacesRule(),
                        NoSemicolonsRule(),
                        NoTrailingSpacesRule(),
                        NoUnitReturnRule(),
                        NoUnusedImportsRule(),
                        NoWildcardImportsRule(),
                        ParameterListWrappingRule(),
                        SpacingAroundColonRule(),
                        SpacingAroundCommaRule(),
                        SpacingAroundCurlyRule(),
                        SpacingAroundDotRule(),
                        SpacingAroundKeywordRule(),
                        SpacingAroundOperatorsRule(),
                        SpacingAroundParensRule(),
                        SpacingAroundRangeOperatorRule(),
                        StringTemplateRule()
                    )
                )
            }
    }
}
