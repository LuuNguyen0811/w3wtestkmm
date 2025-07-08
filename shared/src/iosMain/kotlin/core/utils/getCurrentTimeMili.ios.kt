package core.utils

import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
actual fun getCurrentTimeMili(): Long {
    return Clock.System.now().epochSeconds
}