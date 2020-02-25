![k4][img-k4]


# K4

[![CircleCI][img-circleci]][www-circleci]
[![License][img-license]][www-license]
[![Gitter][img-gitter]][www-gitter]

Welcome to **K4**, a from-scratch template engine inspired by .NET's [T4 (Text Template Transformation Toolkit)](https://github.com/mono/t4) engine.

## Syntax


### Code Tag

```
<#! code #>
```

### Echo Tag

```
<#= code #>
```

### Info Tag

```
<#@ name [ properties ] #>
```

## Internals

TODO

## Plugins

K4 exposes a "plugin" system to allow extending the functionality of the markup, and support for other execution languages.

### Groovy, [`k4-lang-groovy`](./k4-lang-groovy)

This plugin adds support for Groovy as an execution language.

```
<#@ template language="groovy" #>
<#!

def names = ["Foo", "Bar", "Qux"]

#>

Hello world!

<#! names.each { name -> #>

Hello to you, <#= name #>!

<# } #>

Goodbye world!
```

```groovy

```

### Kotlin, [`k4-lang-groovy`](./k4-lang-kotlin)

This plugin adds support for Kotlin as an execution language.

```
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

### Custom Plugins

TODO

## License

The MIT License (MIT). Please see the package [license file][www-license] for more information.

[www-circleci]: https://circleci.com/gh/luggsoft/k4
[img-circleci]: https://circleci.com/gh/luggsoft/k4.svg?style=shield&circle-token=98517cbdff7f1f386d2062704d76abcf863fe2ad
[www-license]: LICENSE.md
[img-license]: https://img.shields.io/badge/license-MIT-blue.svg
[img-gitter]: https://img.shields.io/gitter/room/luggsoft/luggsoft-k4
[www-gitter]: https://gitter.im/luggsoft-k4/community
[img-gitter]: https://badges.gitter.im/luggsoft-k4/community.svg

[img-k4]: assets/luggsoft-k4-t4-for-kotlin.jpg
