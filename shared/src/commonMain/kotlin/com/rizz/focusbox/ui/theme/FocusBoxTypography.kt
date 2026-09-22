package com.rizz.focusbox.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import focusbox.shared.generated.resources.Res
import focusbox.shared.generated.resources.roboto_medium
import focusbox.shared.generated.resources.roboto_regular
import org.jetbrains.compose.resources.Font

@Composable
fun focusBoxFontFamily(): FontFamily = FontFamily(
    Font(Res.font.roboto_regular, weight = FontWeight.Normal),
    Font(Res.font.roboto_medium, weight = FontWeight.Medium),
)

/**
 * The Figma type scale ("Typography - Roboto" in the Components page) maps almost exactly onto
 * Material 3's default roles at their default sizes; only fontFamily is overridden here. The one
 * outlier - the 76sp "Display - timer" style used for the running timer digits - has no M3 role
 * at that size, so it lives separately as [focusBoxDisplayTimerStyle] rather than distorting
 * [Typography.displayLarge] (57sp by default) for every other display-sized use.
 */
@Composable
fun focusBoxTypography(): Typography {
    val family = focusBoxFontFamily()
    val base = Typography()
    return base.copy(
        headlineMedium = base.headlineMedium.copy(fontFamily = family, fontWeight = FontWeight.Normal, fontSize = 28.sp),
        titleLarge = base.titleLarge.copy(fontFamily = family, fontWeight = FontWeight.Medium, fontSize = 22.sp),
        titleMedium = base.titleMedium.copy(fontFamily = family, fontWeight = FontWeight.Medium, fontSize = 16.sp),
        bodyLarge = base.bodyLarge.copy(fontFamily = family, fontWeight = FontWeight.Normal, fontSize = 16.sp),
        bodyMedium = base.bodyMedium.copy(fontFamily = family, fontWeight = FontWeight.Normal, fontSize = 14.sp),
        labelLarge = base.labelLarge.copy(fontFamily = family, fontWeight = FontWeight.Medium, fontSize = 14.sp),
        labelSmall = base.labelSmall.copy(fontFamily = family, fontWeight = FontWeight.Medium, fontSize = 12.sp),
    )
}

/** "Display - timer - 76/Regular" from Figma: the large running-timer digits, not a generic M3 role. */
@Composable
fun focusBoxDisplayTimerStyle(): TextStyle = TextStyle(
    fontFamily = focusBoxFontFamily(),
    fontWeight = FontWeight.Normal,
    fontSize = 76.sp,
)
