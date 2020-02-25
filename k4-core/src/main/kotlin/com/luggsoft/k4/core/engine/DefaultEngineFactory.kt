package com.luggsoft.k4.core.engine

class DefaultEngineFactory : EngineFactoryBase()
{
    override fun createEngineBuilder(): EngineBuilder = DefaultEngineBuilder()
}
