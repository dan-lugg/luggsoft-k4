<!--suppress HtmlDeprecatedAttribute -->

<p align="center">
    <img src="/.github/assets/ls-k4-banner.png?raw=true" alt="ls-k4" />
</p>

---

<p align="center">
    <img src="https://img.shields.io/github/stars/dan-lugg/ls-k4?style=for-the-badge" alt="Stars" />
    <img src="https://img.shields.io/github/license/dan-lugg/ls-k4?style=for-the-badge" alt="License" />
    <img src="https://img.shields.io/github/v/release/dan-lugg/ls-k4?style=for-the-badge" alt="Release" />
    <img src="https://img.shields.io/github/actions/workflow/status/dan-lugg/ls-k4/gradle-publish.yml?style=for-the-badge" alt="Status" />
</p>

# K4, Kotlin T4 Text Transformations

Welcome to **K4**, a from-scratch template engine inspired by [.NET's T4 (Text Template Transformation Toolkit)](https://github.com/mono/t4) engine.

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

The MIT License (MIT). Please see the package [license file](./LICENSE) for more information.
