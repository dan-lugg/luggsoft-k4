package com.luggsoft.k4.cli

import com.luggsoft.k4.cli.commands.K4Command
import com.luggsoft.k4.core.DefaultEngine
import com.luggsoft.k4.core.Engine
import org.springframework.beans.factory.config.BeanDefinition
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.ExitCodeGenerator
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Scope
import picocli.CommandLine
import picocli.CommandLine.IFactory

@SpringBootApplication
open class K4CommandLineRunner(
    private val command: K4Command,
    private val factory: IFactory
) : CommandLineRunner, ExitCodeGenerator
{
    private var exitCode: Int = 0

    override fun run(vararg args: String?)
    {
        val commandLine = CommandLine(this.command, this.factory)
        this.exitCode = commandLine.execute(*args)
    }

    override fun getExitCode(): Int = this.exitCode

    companion object
    {
        @JvmStatic
        fun main(vararg args: String)
        {
            runApplication<K4CommandLineRunner>(*args)
        }
    }
}

@Configuration
class K4Configuration
{
    @Bean
    @Scope(BeanDefinition.SCOPE_SINGLETON)
    fun engine(): Engine = DefaultEngine.Instance
}
