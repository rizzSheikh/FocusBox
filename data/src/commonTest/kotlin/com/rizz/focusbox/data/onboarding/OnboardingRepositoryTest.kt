package com.rizz.focusbox.data.onboarding

import com.russhwolf.settings.Settings
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class OnboardingRepositoryTest {
    @Test
    fun defaultsToNotOnboarded() {
        val repository = OnboardingRepository(InMemorySettings())
        assertFalse(repository.hasOnboarded.value)
    }

    @Test
    fun persistsOnboardedFlag() {
        val settings = InMemorySettings()
        val repository = OnboardingRepository(settings)
        repository.setOnboarded()
        assertTrue(repository.hasOnboarded.value)

        val restored = OnboardingRepository(settings)
        assertEquals(true, restored.hasOnboarded.value)
    }
}

private class InMemorySettings : Settings {
    private val values = mutableMapOf<String, Any>()

    override fun clear() = values.clear()

    override fun remove(key: String) {
        values.remove(key)
    }

    override fun hasKey(key: String): Boolean = key in values

    override fun putString(key: String, value: String) {
        values[key] = value
    }

    override fun getString(key: String, defaultValue: String): String =
        values[key] as? String ?: defaultValue

    override fun getStringOrNull(key: String): String? = values[key] as? String

    override fun getInt(key: String, defaultValue: Int): Int = values[key] as? Int ?: defaultValue

    override fun putInt(key: String, value: Int) {
        values[key] = value
    }

    override fun getIntOrNull(key: String): Int? = values[key] as? Int

    override fun getLong(key: String, defaultValue: Long): Long = defaultValue

    override fun putLong(key: String, value: Long) = Unit

    override fun getLongOrNull(key: String): Long? = null

    override fun getFloat(key: String, defaultValue: Float): Float = defaultValue

    override fun putFloat(key: String, value: Float) = Unit

    override fun getFloatOrNull(key: String): Float? = null

    override fun getDouble(key: String, defaultValue: Double): Double = defaultValue

    override fun putDouble(key: String, value: Double) = Unit

    override fun getDoubleOrNull(key: String): Double? = null

    override fun getBoolean(key: String, defaultValue: Boolean): Boolean =
        values[key] as? Boolean ?: defaultValue

    override fun putBoolean(key: String, value: Boolean) {
        values[key] = value
    }

    override fun getBooleanOrNull(key: String): Boolean? = values[key] as? Boolean

    override val keys: Set<String> get() = values.keys

    override val size: Int get() = values.size
}
