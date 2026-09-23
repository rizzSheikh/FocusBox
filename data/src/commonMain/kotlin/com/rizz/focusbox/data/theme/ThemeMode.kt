package com.rizz.focusbox.data.theme

enum class ThemeMode {
    System,
    Light,
    Dark;

    companion object {
        fun fromStored(value: String?): ThemeMode =
            entries.firstOrNull { it.name == value } ?: System
    }
}
