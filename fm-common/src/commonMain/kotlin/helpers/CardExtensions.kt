package local.learning.common.helpers

import local.learning.common.models.card.Card

fun Card.isNumberValid(): Boolean = number.matches(Regex("\\d{16}"))
fun Card.isValidForValid(): Boolean = validFor.matches(Regex("\\d{4}-\\d{2}"))
fun Card.isGuidValid(): Boolean = guid.isValid()
fun Card.isOwnerValid(): Boolean = owner.length in 3..50
fun Card.isBankGuidValid(): Boolean = bankGuid.isValid()