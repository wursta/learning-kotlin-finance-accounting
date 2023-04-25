package local.learning.common.helpers

import local.learning.common.models.card.Card

fun Card.isNumberValid(): Boolean = !number.matches(Regex("\\d{4}-\\d{4}-\\d{4}-\\d{4}"))
fun Card.isValidForValid(): Boolean = !validFor.matches(Regex("\\d{4}-\\d{2}"))