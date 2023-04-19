package local.learning.api.serialization.utils.card.strategy

import kotlinx.serialization.KSerializer
import local.learning.api.v1.models.CardDeleteRequest
import local.learning.api.v1.models.IRequest
import kotlin.reflect.KClass

object DeleteRequestStrategy: IRequestStrategy {
    override val discriminator: String = "read"
    override val clazz: KClass<out IRequest> = CardDeleteRequest::class
    override val serializer: KSerializer<out IRequest> = CardDeleteRequest.serializer()
    override fun <T : IRequest> fillDiscriminator(req: T): T {
        require(req is CardDeleteRequest)
        @Suppress("UNCHECKED_CAST")
        return req.copy(requestType = discriminator) as T
    }
}