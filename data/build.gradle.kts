import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidMultiplatformLibrary)
    alias(libs.plugins.koinCompiler)
    alias(libs.plugins.sqldelight)
}

sqldelight {
    databases {
        create("FocusBoxDatabase") {
            packageName.set("com.rizz.focusbox.db")
        }
    }
}

kotlin {
    iosArm64()
    iosSimulatorArm64()

    android {
        namespace = "com.rizz.focusbox.data"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()

        compilerOptions {
            jvmTarget = JvmTarget.JVM_11
        }
        withHostTest {}
    }

    sourceSets {
        androidMain.dependencies {
            implementation(libs.sqldelight.android.driver)
        }
        commonMain.dependencies {
            api(libs.koin.core)
            implementation(libs.koin.annotations)
            implementation(libs.multiplatform.settings)
            implementation(libs.multiplatform.settings.no.arg)
            api(libs.sqldelight.runtime)
            api(libs.sqldelight.coroutines.extensions)
        }
        iosMain.dependencies {
            implementation(libs.sqldelight.native.driver)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.multiplatform.settings)
        }
    }
}

val androidHostTestSourceSet = kotlin.sourceSets.findByName("androidHostTest")
if (androidHostTestSourceSet != null) {
    androidHostTestSourceSet.dependencies {
        implementation(libs.sqldelight.sqlite.driver)
    }
}
