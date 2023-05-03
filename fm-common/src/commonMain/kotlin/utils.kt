package local.learning.common

import kotlinx.datetime.Instant

val INSTANT_NONE = Instant.fromEpochMilliseconds(Long.MIN_VALUE)
val INSTANT_NEGATIVE_INFINITY = Instant.fromEpochMilliseconds(Long.MIN_VALUE)
val INSTANT_POSITIVE_INFINITY = Instant.fromEpochMilliseconds(Long.MAX_VALUE)