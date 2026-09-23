package com.rizz.focusbox.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

private object LightColors {
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

private object DarkColors {
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
    amberContainer = colorFromHex(LightColors.AMBER_CONTAINER),
    onAmberContainer = colorFromHex(LightColors.ON_AMBER_CONTAINER),
)

internal val FocusBoxDarkExtraColors = FocusBoxExtraColors(
    amberContainer = colorFromHex(DarkColors.AMBER_CONTAINER),
    onAmberContainer = colorFromHex(DarkColors.ON_AMBER_CONTAINER),
)

val FocusBoxLightColorScheme = lightColorScheme(
    primary = colorFromHex(LightColors.PRIMARY),
    onPrimary = colorFromHex(LightColors.ON_PRIMARY),
    primaryContainer = colorFromHex(LightColors.PRIMARY_CONTAINER),
    onPrimaryContainer = colorFromHex(LightColors.ON_PRIMARY_CONTAINER),
    secondary = colorFromHex(LightColors.SECONDARY),
    onSecondary = colorFromHex(LightColors.ON_SECONDARY),
    secondaryContainer = colorFromHex(LightColors.SECONDARY_CONTAINER),
    onSecondaryContainer = colorFromHex(LightColors.ON_SECONDARY_CONTAINER),
    tertiary = colorFromHex(LightColors.TERTIARY),
    onTertiary = colorFromHex(LightColors.ON_TERTIARY),
    tertiaryContainer = colorFromHex(LightColors.TERTIARY_CONTAINER),
    onTertiaryContainer = colorFromHex(LightColors.ON_TERTIARY_CONTAINER),
    error = colorFromHex(LightColors.ERROR),
    onError = colorFromHex(LightColors.ON_ERROR),
    errorContainer = colorFromHex(LightColors.ERROR_CONTAINER),
    onErrorContainer = colorFromHex(LightColors.ON_ERROR_CONTAINER),
    background = colorFromHex(LightColors.SURFACE),
    onBackground = colorFromHex(LightColors.ON_SURFACE),
    surface = colorFromHex(LightColors.SURFACE),
    onSurface = colorFromHex(LightColors.ON_SURFACE),
    surfaceVariant = colorFromHex(LightColors.CONT),
    onSurfaceVariant = colorFromHex(LightColors.ON_VAR),
    outline = colorFromHex(LightColors.OUTLINE),
    outlineVariant = colorFromHex(LightColors.OUTLINE_VAR),
    inverseSurface = colorFromHex(LightColors.INVERSE_SURFACE),
    inverseOnSurface = colorFromHex(LightColors.INVERSE_ON_SURFACE),
    inversePrimary = colorFromHex(LightColors.INVERSE_PRIMARY),
    surfaceContainerLowest = colorFromHex(LightColors.LOWEST),
    surfaceContainerLow = colorFromHex(LightColors.LOW),
    surfaceContainer = colorFromHex(LightColors.CONT),
    surfaceContainerHigh = colorFromHex(LightColors.HIGH),
    surfaceContainerHighest = colorFromHex(LightColors.HIGHEST),
)

val FocusBoxDarkColorScheme = darkColorScheme(
    primary = colorFromHex(DarkColors.PRIMARY),
    onPrimary = colorFromHex(DarkColors.ON_PRIMARY),
    primaryContainer = colorFromHex(DarkColors.PRIMARY_CONTAINER),
    onPrimaryContainer = colorFromHex(DarkColors.ON_PRIMARY_CONTAINER),
    secondary = colorFromHex(DarkColors.SECONDARY),
    onSecondary = colorFromHex(DarkColors.ON_SECONDARY),
    secondaryContainer = colorFromHex(DarkColors.SECONDARY_CONTAINER),
    onSecondaryContainer = colorFromHex(DarkColors.ON_SECONDARY_CONTAINER),
    tertiary = colorFromHex(DarkColors.TERTIARY),
    onTertiary = colorFromHex(DarkColors.ON_TERTIARY),
    tertiaryContainer = colorFromHex(DarkColors.TERTIARY_CONTAINER),
    onTertiaryContainer = colorFromHex(DarkColors.ON_TERTIARY_CONTAINER),
    error = colorFromHex(DarkColors.ERROR),
    onError = colorFromHex(DarkColors.ON_ERROR),
    errorContainer = colorFromHex(DarkColors.ERROR_CONTAINER),
    onErrorContainer = colorFromHex(DarkColors.ON_ERROR_CONTAINER),
    background = colorFromHex(DarkColors.SURFACE),
    onBackground = colorFromHex(DarkColors.ON_SURFACE),
    surface = colorFromHex(DarkColors.SURFACE),
    onSurface = colorFromHex(DarkColors.ON_SURFACE),
    surfaceVariant = colorFromHex(DarkColors.CONT),
    onSurfaceVariant = colorFromHex(DarkColors.ON_VAR),
    outline = colorFromHex(DarkColors.OUTLINE),
    outlineVariant = colorFromHex(DarkColors.OUTLINE_VAR),
    inverseSurface = colorFromHex(DarkColors.INVERSE_SURFACE),
    inverseOnSurface = colorFromHex(DarkColors.INVERSE_ON_SURFACE),
    inversePrimary = colorFromHex(DarkColors.INVERSE_PRIMARY),
    surfaceContainerLowest = colorFromHex(DarkColors.LOWEST),
    surfaceContainerLow = colorFromHex(DarkColors.LOW),
    surfaceContainer = colorFromHex(DarkColors.CONT),
    surfaceContainerHigh = colorFromHex(DarkColors.HIGH),
    surfaceContainerHighest = colorFromHex(DarkColors.HIGHEST),
)
