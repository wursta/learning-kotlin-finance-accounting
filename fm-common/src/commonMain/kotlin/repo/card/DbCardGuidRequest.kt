package local.learning.common.repo.card

import local.learning.common.models.LockGuid
import local.learning.common.models.card.CardGuid

data class DbCardGuidRequest(
    val guid: CardGuid,
    val lockGuid: LockGuid = LockGuid.NONE,
)
