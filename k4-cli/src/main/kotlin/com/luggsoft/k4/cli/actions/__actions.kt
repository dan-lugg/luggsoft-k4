package com.luggsoft.k4.cli.actions

import com.luggsoft.common.logger
import org.springframework.context.ApplicationContext
import java.util.function.Supplier
import kotlin.reflect.KClass

interface Action<out TResult>

interface ActionContext

interface ActionHandler<in TAction : Action<TResult>, out TResult>
{
    fun handleAction(action: TAction, actionContext: ActionContext): TResult
}

interface ActionInvoker
{
    fun invokeAction(action: Action<*>, actionContext: ActionContext): Any?
}

class ActionInvokerException : Exception
{
    constructor() : super()
    constructor(message: String) : super(message)
    constructor(message: String, cause: Throwable) : super(message, cause)
}

class DefaultActionInvoker(
    private val actionHandlerProvider: ActionHandlerProvider
) : ActionInvoker
{
    override fun invokeAction(action: Action<*>, actionContext: ActionContext): Any?
    {
        try
        {
            val actionHandler = this.actionHandlerProvider.getActionHandler(action::class)
            return actionHandler.handleAction(action, actionContext)
        }
        catch (exception: Exception)
        {
            ("Failed to invoke action, where action = $action").also { message ->
                this.logger.error(message, exception)
                throw ActionInvokerException(message, exception)
            }
        }
    }
}

interface ActionHandlerProvider
{
    fun getActionHandler(actionClass: KClass<out Action<*>>): ActionHandler<Action<*>, *>
}

class ActionHandlerProviderException : Exception
{
    constructor() : super()
    constructor(message: String) : super(message)
    constructor(message: String, cause: Throwable) : super(message, cause)
}

class DefaultActionHandlerProvider(
    private val actionHandlerSupplierMap: Map<KClass<out Action<*>>, Supplier<ActionHandler<Action<*>, *>>>
) : ActionHandlerProvider
{
    constructor(vararg actionHandlerSupplierPairs: Pair<KClass<out Action<*>>, Supplier<ActionHandler<Action<*>, *>>>) : this(
        actionHandlerSupplierMap = actionHandlerSupplierPairs.toMap()
    )

    override fun getActionHandler(actionClass: KClass<out Action<*>>): ActionHandler<Action<*>, *>
    {
        val actionHandlerSupplier = this.actionHandlerSupplierMap[actionClass]
            ?: throw ActionHandlerProviderException("No action handler registered, where action class = $actionClass")
        try
        {
            return actionHandlerSupplier.get()
        }
        catch (exception: Exception)
        {
            ("Failed to get action handler, where action class = $actionClass").also { message ->
                this.logger.error(message, exception)
                throw ActionHandlerProviderException(message, exception)
            }
        }
    }
}

class ApplicationContextActionHandlerProvider(
    applicationContext: ApplicationContext
) : ActionHandlerProvider by DefaultActionHandlerProvider(
    actionHandlerSupplierMap = ApplicationContextActionHandlerProvider.createActionHandlerSupplierMap(applicationContext)
)
{
    companion object
    {
        private fun createActionHandlerSupplierMap(applicationContext: ApplicationContext): Map<KClass<out Action<*>>, Supplier<ActionHandler<Action<*>, *>>>
        {
            return this.getActionHandlerClasses()
                .associateBy(this::getActionClass)
                .mapValues { entry -> this.getActionHandlerSupplier(applicationContext, entry.value) }
        }

        private fun getActionHandlerClasses(): List<KClass<out ActionHandler<Action<*>, *>>>
        {
            TODO("Scan for ActionHandler definitions")
        }

        private fun getActionClass(actionHandlerClass: KClass<out ActionHandler<Action<*>, *>>): KClass<out Action<*>>
        {
            TODO("Extract Action type parameter from ActionHandler class")
        }

        private fun getActionHandlerSupplier(applicationContext: ApplicationContext, actionHandlerClass: KClass<out ActionHandler<Action<*>, *>>): Supplier<ActionHandler<Action<*>, *>>
        {
            return Supplier { applicationContext.getBean(actionHandlerClass.java) }
        }
    }
}
