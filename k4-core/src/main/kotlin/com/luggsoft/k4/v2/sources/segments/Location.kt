package com.luggsoft.k4.v2.sources.segments

data class Location(
    val startIndex: Int,
    val untilIndex: Int,
) : Comparable<Location>
{
    val range: IntRange
        get() = (this.startIndex..this.untilIndex)

    override fun compareTo(other: Location): Int
    {
        if (this.startIndex == other.startIndex)
        {
            return this.untilIndex.compareTo(other.untilIndex)
        }

        return this.startIndex.compareTo(other.startIndex)
    }
}
