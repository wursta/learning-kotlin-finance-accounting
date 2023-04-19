package local.learning.api.serialization.utils.card.strategy

import kotlinx.serialization.KSerializer
import local.learning.api.v1.models.CardUpdateRequest
import local.learning.api.v1.models.IRequest
import kotlin.reflect.KClass

object UpdateRequestStrategy: IRequestStrategy {
    override val discriminator: String = "read"
    override val clazz: KClass<out IRequest> = CardUpdateRequest::class
    override val serializer: KSerializer<out IRequest> = CardUpdateRequest.serializer()
    override fun <T : IRequest> fillDiscriminator(req: T): T {
        require(req is CardUpdateRequest)
        @Suppress("UNCHECKED_CAST")
        return req.copy(requestType = discriminator) as T
    }
}