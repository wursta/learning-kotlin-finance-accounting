package local.learning.app.kafka

import local.learning.api.models.IRequestDto
import local.learning.api.models.IResponseDto
import local.learning.app.biz.IProcessor

data class TopicStrategy(
    val processor: IProcessor,
    val topicsOut: List<String> = emptyList(),
    val block: TopicStrategy.(request: IRequestDto) -> IResponseDto
) {
    fun run(request: IRequestDto): IResponseDto = block(request)
}