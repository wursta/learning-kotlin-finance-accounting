package local.learning.mappers.exceptions

import local.learning.common.CardContext
import kotlin.reflect.KClass

class UnknownRequestClass(clazz: KClass<*>) : Throwable("Class $clazz cannot be mapped to " + CardContext::class)