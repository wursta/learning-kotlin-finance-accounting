package local.kotlin.learning.fm.acceptance

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.request.*
import io.ktor.http.*
import local.learning.api.models.IRequestDto
import local.learning.api.models.IResponseDto
import local.learning.api.serialization.utils.apiRequestSerialize
import local.learning.api.serialization.utils.apiResponseDeserialize
import mu.KotlinLogging

private val log = KotlinLogging.logger {}
object RestClient {
    private val client = HttpClient(OkHttp)
    suspend fun request(path: String, request: IRequestDto): IResponseDto
    {
        val url = "http://${AppCompose.C.hostApp}:${AppCompose.C.portApp}/$path"
        val json = apiRequestSerialize(request)

        log.info { "Send request to $url. Body $json" }

        val resp = client.post {
            url(url)
            headers {
                append(HttpHeaders.ContentType, ContentType.Application.Json)
            }
            accept(ContentType.Application.Json)
            setBody(json)
        }.call

        val status = resp.response.status
        val body: String = resp.body()

        log.info { "Received status: $status, body: $body" }

        return apiResponseDeserialize(body)
    }
}