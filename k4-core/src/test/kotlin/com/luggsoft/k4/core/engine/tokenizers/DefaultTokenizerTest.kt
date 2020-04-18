package com.luggsoft.k4.core.engine.tokenizers

import com.luggsoft.k4.core.Source
import com.luggsoft.k4.core.engine.Location
import com.luggsoft.k4.core.engine.tokenizers.tokens.CodeToken
import com.luggsoft.k4.core.engine.tokenizers.tokens.EchoToken
import com.luggsoft.k4.core.engine.tokenizers.tokens.TextToken
import com.luggsoft.k4.core.engine.tokenizers.tokens.Token
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import kotlin.reflect.KClass

fun trimIndent(input: String): String
{
    return input.trimIndent().trim()
}

internal class DefaultTokenizerTest
{

    @Test
    fun testText()
    {
        val source = Source.fromString(
            name = this::testText.name,
            text = trimIndent(
                input = """
                    Hello world!
                """
            )
        )

        val tokenizer = DefaultTokenizer.Instance
        val tokens = tokenizer.tokenizeSource(source)
        val expectTokens = listOf(
            TextToken(
                text = "Hello world!",
                location = Location(
                    name = this::testText.name,
                    startIndex = 0,
                    untilIndex = 12
                )
            )
        )

        Assertions.assertIterableEquals(expectTokens, tokens)
    }

    @Test
    fun testTextAndEcho()
    {
        val source = Source.fromString(
            name = this::testTextAndEcho.name,
            text = trimIndent(
                input = """
                    Hello world, from <#= "Alice" #>!
                """
            )
        )

        val tokenizer = DefaultTokenizer.Instance
        val tokens = tokenizer.tokenizeSource(source)

        tokens.assertContents(
            0 to (TextToken::class to "Hello world, from "),
            1 to (EchoToken::class to " \"Alice\" "),
            2 to (TextToken::class to "!")
        )
    }

    @Test
    fun testTextEchoAndCode()
    {
        val source = Source.fromString(
            name = this::testTextEchoAndCode.name,
            text = trimIndent(
                input = """
                    <#! (1..3).forEach { number -> #>
                    Hello world number <#= number #>, from <#= "Alice" #>!
                    <#! } #>
                """
            )
        )

        val tokenizer = DefaultTokenizer.Instance
        val tokens = tokenizer.tokenizeSource(source)

        tokens.assertContents(
            0 to (CodeToken::class to " (1..3).forEach { number -> "),
            1 to (TextToken::class to "Hello world number "),
            2 to (EchoToken::class to " number "),
            3 to (TextToken::class to ", from "),
            4 to (EchoToken::class to " \"Alice\" "),
            5 to (TextToken::class to "!\n"),
            6 to (CodeToken::class to " } ")
        )
    }

    private fun List<Token>.assertContents(vararg assertions: Pair<Int, Pair<KClass<*>, String>>)
    {
        assertions.toMap().forEach { (index, pair) ->
            val tokenClass = pair.component1()
            val tokenContent = pair.component2()
            Assertions.assertEquals(tokenClass, this[index]::class)
            Assertions.assertEquals(tokenContent, this[index].content)
        }
    }
}
