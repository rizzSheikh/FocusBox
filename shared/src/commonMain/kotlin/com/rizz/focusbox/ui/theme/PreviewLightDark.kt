package com.rizz.focusbox.ui.theme

import androidx.compose.ui.tooling.preview.AndroidUiModes.UI_MODE_NIGHT_YES
import androidx.compose.ui.tooling.preview.AndroidUiModes.UI_MODE_TYPE_NORMAL
import androidx.compose.ui.tooling.preview.Preview

@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.FUNCTION)
@Preview(
    name = "Light",
    showBackground = true,
    backgroundColor = 0xFFFFF8F6,
)
@Preview(
    name = "Dark",
    showBackground = true,
    backgroundColor = 0xFF1A1110,
    uiMode = UI_MODE_NIGHT_YES or UI_MODE_TYPE_NORMAL,
)
annotation class PreviewLightDark
