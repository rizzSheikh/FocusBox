package com.rizz.focusbox.data.theme

import com.russhwolf.settings.Settings
import kotlin.test.Test
import kotlin.test.assertEquals

class ThemeRepositoryTest {
    @Test
    fun defaultsToSystem() {
        val repository = ThemeRepository(InMemorySettings())
        assertEquals(ThemeMode.System, repository.themeMode.value)
    }

    @Test
    fun persistsThemeMode() {
        val settings = InMemorySettings()
        val repository = ThemeRepository(settings)
        repository.setThemeMode(ThemeMode.Dark)
        assertEquals(ThemeMode.Dark, repository.themeMode.value)

        val restored = ThemeRepository(settings)
        assertEquals(ThemeMode.Dark, restored.themeMode.value)
    }
}

private class InMemorySettings : Settings {
    private val values = mutableMapOf<String, String>()

    override fun clear() = values.clear()

    override fun remove(key: String) {
        values.remove(key)
    }

    override fun hasKey(key: String): Boolean = key in values

    override fun putString(key: String, value: String) {
        values[key] = value
    }

    override fun getString(key: String, defaultValue: String): String =
        values[key] ?: defaultValue

    override fun getStringOrNull(key: String): String? = values[key]

    override fun getInt(key: String, defaultValue: Int): Int = defaultValue

    override fun putInt(key: String, value: Int) = Unit

    override fun getLong(key: String, defaultValue: Long): Long = defaultValue

    override fun putLong(key: String, value: Long) = Unit

    override fun getFloat(key: String, defaultValue: Float): Float = defaultValue

    override fun putFloat(key: String, value: Float) = Unit

    override fun getDouble(key: String, defaultValue: Double): Double = defaultValue

    override fun putDouble(key: String, value: Double) = Unit

    override fun getBoolean(key: String, defaultValue: Boolean): Boolean = defaultValue

    override fun putBoolean(key: String, value: Boolean) = Unit

    override fun getIntOrNull(key: String): Int? = null

    override fun getLongOrNull(key: String): Long? = null

    override fun getFloatOrNull(key: String): Float? = null

    override fun getDoubleOrNull(key: String): Double? = null

    override fun getBooleanOrNull(key: String): Boolean? = null

    override val keys: Set<String> get() = values.keys

    override val size: Int get() = values.size
}
