package local.learning.api.serialization.utils

import kotlinx.serialization.*
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.modules.SerializersModuleBuilder
import kotlinx.serialization.modules.contextual
import local.learning.api.models.IRequestDto
import local.learning.api.models.IResponseDto
import kotlin.reflect.KClass

internal data class PolymorphicInfo<S : Any, T : S>(
    val klass: KClass<T>,
    val superClass: KClass<S>,
    val serialize: (Encoder, Any) -> Unit,
    val discriminator: String,
    val makeCopyWithDiscriminator: (Any) -> T
)

@OptIn(InternalSerializationApi::class)
@Suppress("UNCHECKED_CAST")
internal fun <S : Any, T : S> info(
    klass: KClass<T>,
    superClass: KClass<S>,
    discriminator: String,
    makeCopyWithDiscriminator: T.(String) -> T
) =
    PolymorphicInfo(
        klass,
        superClass,
        { e: Encoder, v: Any ->
            klass.serializer().serialize(e, v as T)
        },
        discriminator,
        { v: Any ->
            makeCopyWithDiscriminator(v as T, discriminator)
        }
    )

private inline fun findInfo(
    klass: KClass<*>,
    error: String,
    predicate: PolymorphicInfo<out Any, out Any>.() -> Boolean
) =
    infos.firstOrNull {
        it.superClass == klass && it.predicate()
    } ?: throw SerializationException(error)


@OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)
private inline fun <reified T : Any> SerializersModuleBuilder.polymorphicSerializer() {
    polymorphicDefaultSerializer(T::class) { value ->
        val info = findInfo(T::class, "Unknown class to serialize ${value::class}") { klass == value::class }
        object : KSerializer<T> {
            override val descriptor: SerialDescriptor
                get() = info.klass.serializer().descriptor

            override fun serialize(encoder: Encoder, value: T) {
                val copy = info.makeCopyWithDiscriminator(value)
                info.serialize(encoder, copy)
            }

            override fun deserialize(decoder: Decoder): T = throw NotImplementedError("you should not use this method")
        }
    }
}

private class PolymorphicSerializer<T : Any>(private val klass: KClass<T>, private val discriminatorField: String) :
    JsonContentPolymorphicSerializer<T>(klass) {
    @OptIn(InternalSerializationApi::class)
    @Suppress("UNCHECKED_CAST")
    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<out T> {
        val discriminatorValue = element.jsonObject[discriminatorField]?.jsonPrimitive?.content
        val info = findInfo(klass, "Unknown class to deserialize: $discriminatorValue") {
            discriminator == discriminatorValue
        }
        return info.klass.serializer() as DeserializationStrategy<out T>
    }
}

private val requestSerializer = PolymorphicSerializer(IRequestDto::class, "requestType")
private val responseSerializer = PolymorphicSerializer(IResponseDto::class, "responseType")

@OptIn(ExperimentalSerializationApi::class)
internal fun SerializersModuleBuilder.setupPolymorphic() {
    polymorphicSerializer<IRequestDto>()
    polymorphicDefaultDeserializer(IRequestDto::class) { requestSerializer }

    polymorphicSerializer<IResponseDto>()
    polymorphicDefaultDeserializer(IResponseDto::class) { responseSerializer }

    contextual(requestSerializer)
    contextual(responseSerializer)
}

fun apiRequestSerialize(request: IRequestDto): String = jsonSerializer.encodeToString(request)
@Suppress("UNCHECKED_CAST")
fun <T : IRequestDto> apiRequestDeserialize(json: String): T = jsonSerializer.decodeFromString<IRequestDto>(json) as T
fun apiResponseSerialize(response: IResponseDto): String = jsonSerializer.encodeToString(response)
@Suppress("UNCHECKED_CAST")
fun <T : IResponseDto> apiResponseDeserialize(json: String): T =
    jsonSerializer.decodeFromString<IResponseDto>(json) as T