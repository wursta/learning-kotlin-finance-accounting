package local.learning.common

import kotlinx.datetime.Instant

internal val INSTANT_NONE = Instant.fromEpochMilliseconds(Long.MIN_VALUE)
internal val INSTANT_NEGATIVE_INFINITY = Instant.fromEpochMilliseconds(Long.MIN_VALUE)
internal val INSTANT_POSITIVE_INFINITY = Instant.fromEpochMilliseconds(Long.MAX_VALUE)