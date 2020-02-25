package com.luggsoft.k4.cli.commands

import java.util.concurrent.Callable

interface CliCommand : Callable<Int>
