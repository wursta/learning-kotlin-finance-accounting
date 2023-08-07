package local.learning.common.helpers

import local.learning.common.models.Principal
import local.learning.common.models.card.Card
import local.learning.common.models.card.CardGuid
import local.learning.common.models.card.CardRelation

fun Card.isNumberValid(): Boolean = number.matches(Regex("\\d{16}"))
fun Card.isValidForValid(): Boolean = validFor.matches(Regex("\\d{4}-\\d{2}"))
fun Card.isGuidValid(): Boolean = guid.isValid()
fun Card.isOwnerValid(): Boolean = owner.length in 5..50
fun Card.isBankGuidValid(): Boolean = bankGuid.isValid()
fun Card.resolveRelationsTo(principal: Principal): Set<CardRelation> = setOfNotNull(
    CardRelation.NONE,
    CardRelation.NEW.takeIf { guid == CardGuid.NONE },
    CardRelation.OWN.takeIf { principal.id == createdBy }
)