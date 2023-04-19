package local.learning.api.serialization.utils.card.strategy

import kotlinx.serialization.KSerializer
import local.learning.api.v1.models.CardCreateRequest
import local.learning.api.v1.models.IRequest
import kotlin.reflect.KClass

object CreateRequestStrategy: IRequestStrategy {
    override val discriminator: String = "create"
    override val clazz: KClass<out IRequest> = CardCreateRequest::class
    override val serializer: KSerializer<out IRequest> = CardCreateRequest.serializer()
    override fun <T : IRequest> fillDiscriminator(req: T): T {
        require(req is CardCreateRequest)
        @Suppress("UNCHECKED_CAST")
        return req.copy(requestType = discriminator) as T
    }
}