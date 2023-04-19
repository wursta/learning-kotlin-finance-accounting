package local.learning.api.serialization.utils.card.strategy

import kotlinx.serialization.KSerializer
import local.learning.api.v1.models.CardReadRequest
import local.learning.api.v1.models.IRequest
import kotlin.reflect.KClass

object ReadRequestStrategy: IRequestStrategy {
    override val discriminator: String = "read"
    override val clazz: KClass<out IRequest> = CardReadRequest::class
    override val serializer: KSerializer<out IRequest> = CardReadRequest.serializer()
    override fun <T : IRequest> fillDiscriminator(req: T): T {
        require(req is CardReadRequest)
        @Suppress("UNCHECKED_CAST")
        return req.copy(requestType = discriminator) as T
    }
}