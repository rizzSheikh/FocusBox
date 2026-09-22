package com.rizz.focusbox.ui.theme

import androidx.compose.ui.graphics.Color

internal fun colorFromHex(hex: String): Color {
    val normalized = hex.removePrefix("#")
    require(normalized.length == 6 || normalized.length == 8) {
        "Expected #RRGGBB or #AARRGGBB, got $hex"
    }
    val value = normalized.toLong(16)
    return if (normalized.length == 6) {
        Color(
            red = ((value shr 16) and 0xFF) / 255f,
            green = ((value shr 8) and 0xFF) / 255f,
            blue = (value and 0xFF) / 255f,
            alpha = 1f,
        )
    } else {
        Color(
            alpha = ((value shr 24) and 0xFF) / 255f,
            red = ((value shr 16) and 0xFF) / 255f,
            green = ((value shr 8) and 0xFF) / 255f,
            blue = (value and 0xFF) / 255f,
        )
    }
}
