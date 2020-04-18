package com.luggsoft.common

import org.intellij.lang.annotations.Language
import java.io.File

fun Int.asTypeParams(modifier: String = String()): String = (1..this)
    .joinToString { index -> "$modifier TParam$index".trim() }
    .trim()

fun Int.asParams(modifier: String = String()): String = (1..this)
    .joinToString { index -> "$modifier param$index: TParam$index".trim() }
    .trim()

fun Int.asArgs(): String = (1..this)
    .joinToString { index -> "param$index" }

@Language("kotlin")
fun generatePartial(x: Int, y: Int): String = """
    
"""

@Language("kotlin")
fun generateMemoize(x: Int): String = """

/**
* TODO
*
* @param missingValue
* @constructor
* TODO
*
* @param missingValue
*/
data class Tuple$x<${x.asTypeParams("out")}>(${x.asParams("val")})

/**
* TODO
*
* @param missingValue
* @param TResult
* @param tuplex
* @return
*/
fun <${x.asTypeParams()}, TResult> ((${x.asTypeParams()}) -> TResult).invokeWithTuple(tuple$x: Tuple$x<${x.asTypeParams()}>): TResult = this
    .invoke(${(1..x).joinToString { i -> "tuple$x.param$i" }})

/**
* TODO
*
* @param missingValue
* @param TResult
* @return
*/
fun <${x.asTypeParams()}, TResult> ((${x.asTypeParams()}) -> TResult).memoize(): Memoized$x<${x.asTypeParams()}, TResult> = Memoized$x(this)

/**
* TODO
*
* @param missingValue
* @param TResult
* @param missingValue
* @return
*/
fun <${x.asTypeParams()}, TResult> ((${x.asTypeParams()}) -> TResult).partial(${x.asParams()}): () -> TResult = TODO() 

/**
* TODO
*
* @param missingValue
* @param TResult
* @property block
* @property cache
*/
data class Memoized$x<${x.asTypeParams()}, out TResult>(
    private val block: (${x.asTypeParams()}) -> TResult,
    private val cache: ConcurrentMap<Tuple$x<${x.asTypeParams()}>, TResult> = ConcurrentHashMap()
) : (${x.asTypeParams()}) -> TResult, Map<Tuple$x<${x.asTypeParams()}>, TResult> by cache
{
    /**
    * TODO
    *
    * @param missingValue
    * @return
    */
    override fun invoke(${x.asParams()}): TResult
    {
        val key = Tuple$x(${x.asArgs()})
        val provider = this.block.partial(${x.asArgs()})
        return this.cache.getOrPut(key, provider)
    }
}

"""

fun <TElement> Collection<TElement>.unrepeat(): Sequence<TElement>
{
    return sequence {
        var lastElement: TElement? = null
        loop@ for (element in this@unrepeat)
        {
            if (element == lastElement)
            {
                continue@loop
            }
            lastElement = element
            this@sequence.yield(element)
        }
    }
}

fun <TElement> List<TElement>.unrepeat(): List<TElement> = (this as Collection<TElement>)
    .unrepeat()
    .toList()

internal fun main()
{
    val file = File("common/src/main/kotlin/com/luggsoft/common/__output.kt")
        .also { file ->
            file.writeText("")
        }

    val writer = file.writer()

    writer.appendln("package com.luggsoft.common")
    writer.appendln("import java.util.concurrent.ConcurrentHashMap")
    writer.appendln("import java.util.concurrent.ConcurrentMap")

    (1..3)
        .joinToString(separator = "\n", transform = ::generateMemoize)
        .lines()
        .map(String::trimEnd)
        .unrepeat()
        .forEach { line ->
            writer.appendln(line)
        }

    writer.flush()
}
