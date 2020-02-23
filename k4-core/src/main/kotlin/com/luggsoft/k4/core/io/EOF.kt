package com.luggsoft.k4.core.io

import com.luggsoft.k4.core.util.NEGATIVE_ONE

val Char.Companion.EOF: Char by lazy { Int.NEGATIVE_ONE.toChar() }
