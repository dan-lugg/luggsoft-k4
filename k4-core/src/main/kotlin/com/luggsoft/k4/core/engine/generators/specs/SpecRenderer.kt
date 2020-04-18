package com.luggsoft.k4.core.engine.generators.specs

class SpecRenderer
{
    fun renderFileSpec(fileSpec: FileSpec): String
    {
        val lineSequence = sequence {
            this.yield("package ${fileSpec.packageName}")
            this.yield("")


            val importLineSequence = sequence {
                val importTypes = (fileSpec.importTypes +
                        fileSpec.functionSpecs.map(FunctionSpec::returnType) +
                        fileSpec.functionSpecs.flatMap { functionSpec -> functionSpec.parameterSpecs.map(ParameterSpec::type) })
                    .distinct()

                importTypes.forEach { importType ->
                    this.yield("import ${importType.qualifiedName}")
                }
            }
            this.yieldAll(importLineSequence)

            fileSpec.functionSpecs.forEach { functionSpec ->
                this.yield("fun ${functionSpec.name} (")

                val parameterSequence = sequence {
                    functionSpec.parameterSpecs.forEach { parameterSpec ->
                        this.yield("${parameterSpec.name}: ${parameterSpec.type.simpleName}")
                    }
                }
                val parameterLine = parameterSequence.joinToString(separator = ", ")
                this.yield(parameterLine)

                this.yield("): ${functionSpec.returnType.simpleName}")
                this.yield("{")
                this.yield(functionSpec.code)
                this.yield("}")
                this.yield("")
            }
        }

        return lineSequence.joinToString(separator = "\n")
    }
}
