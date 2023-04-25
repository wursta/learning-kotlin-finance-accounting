package local.learning.mappers.v1.exceptions

import local.learning.common.models.card.CardContext
import kotlin.reflect.KClass

class UnknownRequestClass(clazz: KClass<*>) : Throwable("Class $clazz cannot be mapped to " + CardContext::class)