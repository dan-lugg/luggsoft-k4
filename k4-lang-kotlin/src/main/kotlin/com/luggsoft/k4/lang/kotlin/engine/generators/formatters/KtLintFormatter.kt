package com.luggsoft.k4.lang.kotlin.engine.generators.formatters

import com.luggsoft.common.logger
import com.pinterest.ktlint.core.KtLint
import com.pinterest.ktlint.core.KtLint.Params
import com.pinterest.ktlint.core.LintError
import com.pinterest.ktlint.core.RuleSet
import com.pinterest.ktlint.ruleset.experimental.ExperimentalRuleSetProvider
import com.pinterest.ktlint.ruleset.standard.StandardRuleSetProvider

class KtLintFormatter(
    private val ruleSets: List<RuleSet> = KtLintFormatter.ruleSets,
    private val script: Boolean = true,
    private val debug: Boolean = true
) : FormatterBase()
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

    private fun getParams(input: String): Params = Params(
        text = input,
        ruleSets = this.ruleSets,
        debug = this.debug,
        script = this.script,
        cb = { lintError: LintError, corrected: Boolean ->
            this.logger.debug("KtLint error, ${lintError.ruleId} ${lintError.detail} @ [${lintError.line}:${lintError.col}")
        }
    )

    private companion object
    {
        val ruleSets: List<RuleSet>
            get() = listOf(
                StandardRuleSetProvider().get(),
                ExperimentalRuleSetProvider().get()
            )
    }
}
