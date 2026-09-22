package com.rizz.focusbox.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

private object LightTokens {
    const val PRIMARY = "#BB1614"
    const val ON_PRIMARY = "#FFFFFF"
    const val PRIMARY_CONTAINER = "#FFDAD5"
    const val ON_PRIMARY_CONTAINER = "#410001"
    const val SECONDARY = "#775652"
    const val ON_SECONDARY = "#FFFFFF"
    const val SECONDARY_CONTAINER = "#FFDAD5"
    const val ON_SECONDARY_CONTAINER = "#2C1512"
    const val TERTIARY = "#006A60"
    const val ON_TERTIARY = "#FFFFFF"
    const val TERTIARY_CONTAINER = "#9EF2E4"
    const val ON_TERTIARY_CONTAINER = "#00201C"
    const val AMBER_CONTAINER = "#FFDDB3"
    const val ON_AMBER_CONTAINER = "#291800"
    const val ERROR = "#BA1A1A"
    const val ON_ERROR = "#FFFFFF"
    const val ERROR_CONTAINER = "#FFDAD6"
    const val ON_ERROR_CONTAINER = "#601410"
    const val SURFACE = "#FFF8F6"
    const val LOWEST = "#FFFFFF"
    const val LOW = "#FFF0EE"
    const val CONT = "#FCEAE7"
    const val HIGH = "#F6E4E1"
    const val HIGHEST = "#F1DEDB"
    const val ON_SURFACE = "#231918"
    const val ON_VAR = "#534341"
    const val OUTLINE = "#857370"
    const val OUTLINE_VAR = "#D8C2BE"
    const val INVERSE_SURFACE = "#392E2C"
    const val INVERSE_ON_SURFACE = "#FFEDEA"
    const val INVERSE_PRIMARY = "#FFB4A8"
}

private object DarkTokens {
    const val PRIMARY = "#FFB4A8"
    const val ON_PRIMARY = "#690100"
    const val PRIMARY_CONTAINER = "#8C1D18"
    const val ON_PRIMARY_CONTAINER = "#FFDAD5"
    const val SECONDARY = "#E7BDB7"
    const val ON_SECONDARY = "#442925"
    const val SECONDARY_CONTAINER = "#5D3F3B"
    const val ON_SECONDARY_CONTAINER = "#FFDAD5"
    const val TERTIARY = "#82D5C8"
    const val ON_TERTIARY = "#003731"
    const val TERTIARY_CONTAINER = "#005048"
    const val ON_TERTIARY_CONTAINER = "#9EF2E4"
    const val AMBER_CONTAINER = "#633F10"
    const val ON_AMBER_CONTAINER = "#FFDDB3"
    const val ERROR = "#FFB4AB"
    const val ON_ERROR = "#690005"
    const val ERROR_CONTAINER = "#93000A"
    const val ON_ERROR_CONTAINER = "#FFDAD6"
    const val SURFACE = "#1A1110"
    const val LOWEST = "#140C0B"
    const val LOW = "#231918"
    const val CONT = "#271D1C"
    const val HIGH = "#322827"
    const val HIGHEST = "#3D3231"
    const val ON_SURFACE = "#F1DFDC"
    const val ON_VAR = "#D8C2BE"
    const val OUTLINE = "#A08C89"
    const val OUTLINE_VAR = "#534341"
    const val INVERSE_SURFACE = "#F1DFDC"
    const val INVERSE_ON_SURFACE = "#392E2C"
    const val INVERSE_PRIMARY = "#BB1614"
}

data class FocusBoxExtraColors(
    val amberContainer: Color,
    val onAmberContainer: Color,
)

internal val FocusBoxLightExtraColors = FocusBoxExtraColors(
    amberContainer = colorFromHex(LightTokens.AMBER_CONTAINER),
    onAmberContainer = colorFromHex(LightTokens.ON_AMBER_CONTAINER),
)

internal val FocusBoxDarkExtraColors = FocusBoxExtraColors(
    amberContainer = colorFromHex(DarkTokens.AMBER_CONTAINER),
    onAmberContainer = colorFromHex(DarkTokens.ON_AMBER_CONTAINER),
)

val FocusBoxLightColorScheme = lightColorScheme(
    primary = colorFromHex(LightTokens.PRIMARY),
    onPrimary = colorFromHex(LightTokens.ON_PRIMARY),
    primaryContainer = colorFromHex(LightTokens.PRIMARY_CONTAINER),
    onPrimaryContainer = colorFromHex(LightTokens.ON_PRIMARY_CONTAINER),
    secondary = colorFromHex(LightTokens.SECONDARY),
    onSecondary = colorFromHex(LightTokens.ON_SECONDARY),
    secondaryContainer = colorFromHex(LightTokens.SECONDARY_CONTAINER),
    onSecondaryContainer = colorFromHex(LightTokens.ON_SECONDARY_CONTAINER),
    tertiary = colorFromHex(LightTokens.TERTIARY),
    onTertiary = colorFromHex(LightTokens.ON_TERTIARY),
    tertiaryContainer = colorFromHex(LightTokens.TERTIARY_CONTAINER),
    onTertiaryContainer = colorFromHex(LightTokens.ON_TERTIARY_CONTAINER),
    error = colorFromHex(LightTokens.ERROR),
    onError = colorFromHex(LightTokens.ON_ERROR),
    errorContainer = colorFromHex(LightTokens.ERROR_CONTAINER),
    onErrorContainer = colorFromHex(LightTokens.ON_ERROR_CONTAINER),
    background = colorFromHex(LightTokens.SURFACE),
    onBackground = colorFromHex(LightTokens.ON_SURFACE),
    surface = colorFromHex(LightTokens.SURFACE),
    onSurface = colorFromHex(LightTokens.ON_SURFACE),
    surfaceVariant = colorFromHex(LightTokens.CONT),
    onSurfaceVariant = colorFromHex(LightTokens.ON_VAR),
    outline = colorFromHex(LightTokens.OUTLINE),
    outlineVariant = colorFromHex(LightTokens.OUTLINE_VAR),
    inverseSurface = colorFromHex(LightTokens.INVERSE_SURFACE),
    inverseOnSurface = colorFromHex(LightTokens.INVERSE_ON_SURFACE),
    inversePrimary = colorFromHex(LightTokens.INVERSE_PRIMARY),
    surfaceContainerLowest = colorFromHex(LightTokens.LOWEST),
    surfaceContainerLow = colorFromHex(LightTokens.LOW),
    surfaceContainer = colorFromHex(LightTokens.CONT),
    surfaceContainerHigh = colorFromHex(LightTokens.HIGH),
    surfaceContainerHighest = colorFromHex(LightTokens.HIGHEST),
)

val FocusBoxDarkColorScheme = darkColorScheme(
    primary = colorFromHex(DarkTokens.PRIMARY),
    onPrimary = colorFromHex(DarkTokens.ON_PRIMARY),
    primaryContainer = colorFromHex(DarkTokens.PRIMARY_CONTAINER),
    onPrimaryContainer = colorFromHex(DarkTokens.ON_PRIMARY_CONTAINER),
    secondary = colorFromHex(DarkTokens.SECONDARY),
    onSecondary = colorFromHex(DarkTokens.ON_SECONDARY),
    secondaryContainer = colorFromHex(DarkTokens.SECONDARY_CONTAINER),
    onSecondaryContainer = colorFromHex(DarkTokens.ON_SECONDARY_CONTAINER),
    tertiary = colorFromHex(DarkTokens.TERTIARY),
    onTertiary = colorFromHex(DarkTokens.ON_TERTIARY),
    tertiaryContainer = colorFromHex(DarkTokens.TERTIARY_CONTAINER),
    onTertiaryContainer = colorFromHex(DarkTokens.ON_TERTIARY_CONTAINER),
    error = colorFromHex(DarkTokens.ERROR),
    onError = colorFromHex(DarkTokens.ON_ERROR),
    errorContainer = colorFromHex(DarkTokens.ERROR_CONTAINER),
    onErrorContainer = colorFromHex(DarkTokens.ON_ERROR_CONTAINER),
    background = colorFromHex(DarkTokens.SURFACE),
    onBackground = colorFromHex(DarkTokens.ON_SURFACE),
    surface = colorFromHex(DarkTokens.SURFACE),
    onSurface = colorFromHex(DarkTokens.ON_SURFACE),
    surfaceVariant = colorFromHex(DarkTokens.CONT),
    onSurfaceVariant = colorFromHex(DarkTokens.ON_VAR),
    outline = colorFromHex(DarkTokens.OUTLINE),
    outlineVariant = colorFromHex(DarkTokens.OUTLINE_VAR),
    inverseSurface = colorFromHex(DarkTokens.INVERSE_SURFACE),
    inverseOnSurface = colorFromHex(DarkTokens.INVERSE_ON_SURFACE),
    inversePrimary = colorFromHex(DarkTokens.INVERSE_PRIMARY),
    surfaceContainerLowest = colorFromHex(DarkTokens.LOWEST),
    surfaceContainerLow = colorFromHex(DarkTokens.LOW),
    surfaceContainer = colorFromHex(DarkTokens.CONT),
    surfaceContainerHigh = colorFromHex(DarkTokens.HIGH),
    surfaceContainerHighest = colorFromHex(DarkTokens.HIGHEST),
)
