package com.luggsoft.k4.example

class MyModel
{
    val name: String = "MyExample"
    val methods: List<Method> = listOf(
        Method(),
        Method(),
        Method()
    )

    class Method
    {
        val name: String = "MyMethod"
    }
}
