package com.luggsoft.k4.core.templates.identifiers

data class Identifier(
    private val name: String
)
{
    private val segments: List<String> = regex.findAll(this.name)
        .map(MatchResult::value)
        .toList()

    fun toCase(casing: Casing): String = casing.apply(this.segments)

    fun toUpperCamelCase(): String = UpperCamelCasing.apply(this.segments)

    fun toLowerCamelCase(): String = LowerCamelCasing.apply(this.segments)

    fun toUpperSnakeCase(): String = UpperSnakeCasing.apply(this.segments)

    fun toLowerSnakeCase(): String = LowerSnakeCasing.apply(this.segments)

    fun toUpperKebabCase(): String = UpperKebabCasing.apply(this.segments)

    fun toLowerKebabCase(): String = LowerKebabCasing.apply(this.segments)

    companion object
    {
        private const val PATTERN = "([A-Z]+(?![a-z])|[A-Z][a-z]+|[A-Z]+|[a-z]+|[0-9]+)"

        private val regex = Regex(PATTERN)
    }
}
