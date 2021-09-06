![K4, T4 for Kotlin][img-header]

# K4, T4 for Kotlin

[![Gradle Package][img-actions]][www-actions]
[![License][img-license]][www-license]

Welcome to **K4**, a from-scratch template engine inspired by .NET's [T4 (Text Template Transformation Toolkit)](https://github.com/mono/t4) engine.

## Syntax

```k4
<#@ template language="kotlin" #>
<#!

val names = listOf("Foo", "Bar", "Qux")

#>

Hello world!

<#! names.forEach { name -> #>

Hello to you, <#= name #>!

<# } #>

Goodbye world!
```

### Code Tag

```k4
<#! code #>
```

### Echo Tag

```k4
<#= code #>
```

### Info Tag

```k4
<#@ name [ properties ] #>
```

## Internals

TODO

### Custom Plugins

TODO

## License

The MIT License (MIT). Please see the package [license file][www-license] for more information.

[img-header]: assets/header.jpg

[www-license]: LICENSE.md

[img-license]: https://img.shields.io/badge/license-MIT-blue.svg

[img-actions]: https://github.com/dan-lugg/kt-k4/actions/workflows/gradle-publish.yml/badge.svg

[www-actions]: https://github.com/dan-lugg/kt-k4/actions/workflows/gradle-publish.yml
