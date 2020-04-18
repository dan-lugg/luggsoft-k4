package com.luggsoft.common

import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap

/**
 * TODO
 *
 * @param missingValue
 * @constructor
 * TODO
 *
 * @param missingValue
 */
data class Tuple1<out TParam1>(val param1: TParam1)

/**
 * TODO
 *
 * @param missingValue
 * @param TResult
 * @param tuplex
 * @return
 */
fun <TParam1, TResult> ((TParam1) -> TResult).invokeWithTuple(tuple1: Tuple1<TParam1>): TResult = this
    .invoke(tuple1.param1)

/**
 * TODO
 *
 * @param missingValue
 * @param TResult
 * @return
 */
fun <TParam1, TResult> ((TParam1) -> TResult).memoize(): Memoized1<TParam1, TResult> = Memoized1(this)

/**
 * TODO
 *
 * @param missingValue
 * @param TResult
 * @param missingValue
 * @return
 */
fun <TParam1, TResult> ((TParam1) -> TResult).partial(param1: TParam1): () -> TResult = TODO()

/**
 * TODO
 *
 * @param missingValue
 * @param TResult
 * @property block
 * @property cache
 */
data class Memoized1<TParam1, out TResult>(
    private val block: (TParam1) -> TResult,
    private val cache: ConcurrentMap<Tuple1<TParam1>, TResult> = ConcurrentHashMap()
) : (TParam1) -> TResult, Map<Tuple1<TParam1>, TResult> by cache
{
    /**
     * TODO
     *
     * @param missingValue
     * @return
     */
    override fun invoke(param1: TParam1): TResult
    {
        val key = Tuple1(param1)
        val provider = this.block.partial(param1)
        return this.cache.getOrPut(key, provider)
    }
}

/**
 * TODO
 *
 * @param missingValue
 * @constructor
 * TODO
 *
 * @param missingValue
 */
data class Tuple2<out TParam1, out TParam2>(val param1: TParam1, val param2: TParam2)

/**
 * TODO
 *
 * @param missingValue
 * @param TResult
 * @param tuplex
 * @return
 */
fun <TParam1, TParam2, TResult> ((TParam1, TParam2) -> TResult).invokeWithTuple(tuple2: Tuple2<TParam1, TParam2>): TResult = this
    .invoke(tuple2.param1, tuple2.param2)

/**
 * TODO
 *
 * @param missingValue
 * @param TResult
 * @return
 */
fun <TParam1, TParam2, TResult> ((TParam1, TParam2) -> TResult).memoize(): Memoized2<TParam1, TParam2, TResult> = Memoized2(this)

/**
 * TODO
 *
 * @param missingValue
 * @param TResult
 * @param missingValue
 * @return
 */
fun <TParam1, TParam2, TResult> ((TParam1, TParam2) -> TResult).partial(param1: TParam1, param2: TParam2): () -> TResult = TODO()

/**
 * TODO
 *
 * @param missingValue
 * @param TResult
 * @property block
 * @property cache
 */
data class Memoized2<TParam1, TParam2, out TResult>(
    private val block: (TParam1, TParam2) -> TResult,
    private val cache: ConcurrentMap<Tuple2<TParam1, TParam2>, TResult> = ConcurrentHashMap()
) : (TParam1, TParam2) -> TResult, Map<Tuple2<TParam1, TParam2>, TResult> by cache
{
    /**
     * TODO
     *
     * @param missingValue
     * @return
     */
    override fun invoke(param1: TParam1, param2: TParam2): TResult
    {
        val key = Tuple2(param1, param2)
        val provider = this.block.partial(param1, param2)
        return this.cache.getOrPut(key, provider)
    }
}

/**
 * TODO
 *
 * @param missingValue
 * @constructor
 * TODO
 *
 * @param missingValue
 */
data class Tuple3<out TParam1, out TParam2, out TParam3>(val param1: TParam1, val param2: TParam2, val param3: TParam3)

/**
 * TODO
 *
 * @param missingValue
 * @param TResult
 * @param tuplex
 * @return
 */
fun <TParam1, TParam2, TParam3, TResult> ((TParam1, TParam2, TParam3) -> TResult).invokeWithTuple(tuple3: Tuple3<TParam1, TParam2, TParam3>): TResult = this
    .invoke(tuple3.param1, tuple3.param2, tuple3.param3)

/**
 * TODO
 *
 * @param missingValue
 * @param TResult
 * @return
 */
fun <TParam1, TParam2, TParam3, TResult> ((TParam1, TParam2, TParam3) -> TResult).memoize(): Memoized3<TParam1, TParam2, TParam3, TResult> = Memoized3(this)

/**
 * TODO
 *
 * @param missingValue
 * @param TResult
 * @param missingValue
 * @return
 */
fun <TParam1, TParam2, TParam3, TResult> ((TParam1, TParam2, TParam3) -> TResult).partial(param1: TParam1, param2: TParam2, param3: TParam3): () -> TResult = TODO()

/**
 * TODO
 *
 * @param missingValue
 * @param TResult
 * @property block
 * @property cache
 */
data class Memoized3<TParam1, TParam2, TParam3, out TResult>(
    private val block: (TParam1, TParam2, TParam3) -> TResult,
    private val cache: ConcurrentMap<Tuple3<TParam1, TParam2, TParam3>, TResult> = ConcurrentHashMap()
) : (TParam1, TParam2, TParam3) -> TResult, Map<Tuple3<TParam1, TParam2, TParam3>, TResult> by cache
{
    /**
     * TODO
     *
     * @param missingValue
     * @return
     */
    override fun invoke(param1: TParam1, param2: TParam2, param3: TParam3): TResult
    {
        val key = Tuple3(param1, param2, param3)
        val provider = this.block.partial(param1, param2, param3)
        return this.cache.getOrPut(key, provider)
    }
}

