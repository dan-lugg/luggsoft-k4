package com.luggsoft.k4.cli.helpers

interface TargetDeterminer
{
    fun determineTarget(descriptor: String): Target
}

