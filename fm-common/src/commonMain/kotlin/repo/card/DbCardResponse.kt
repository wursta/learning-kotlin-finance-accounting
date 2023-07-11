package local.learning.common.repo.card

import local.learning.common.models.Error
import local.learning.common.models.card.Card

data class DbCardResponse(
    val card: Card?,
    val success: Boolean,
    val errors: List<Error> = emptyList()
)
