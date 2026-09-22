package com.rizz.focusbox.feature.timer

import platform.Foundation.*

internal actual fun currentTimeMillis(): Long = (NSDate().timeIntervalSince1970 * 1000).toLong()
