package local.learning.api.serialization.utils.card

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import local.learning.api.v1.models.*

@OptIn(ExperimentalSerializationApi::class)
val jsonSerializer = Json {
    classDiscriminator = "_"
    encodeDefaults = true
    ignoreUnknownKeys = true

    serializersModule = SerializersModule {
        polymorphicDefaultSerializer(IRequest::class) {
            @Suppress("UNCHECKED_CAST")
            when (it) {
                is CardCreateRequest -> RequestSerializer(CardCreateRequest.serializer()) as SerializationStrategy<IRequest>
                is CardReadRequest -> RequestSerializer(CardReadRequest.serializer()) as SerializationStrategy<IRequest>
                is CardUpdateRequest -> RequestSerializer(CardUpdateRequest.serializer()) as SerializationStrategy<IRequest>
                is CardDeleteRequest -> RequestSerializer(CardDeleteRequest.serializer()) as SerializationStrategy<IRequest>
                else -> null
            }
        }
        polymorphicDefault(IRequest::class) {
            CardRequestSerializer
        }
    }
}
