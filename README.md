This is a Kotlin Multiplatform project targeting Android, iOS.

* [/iosApp](./iosApp/iosApp) contains an iOS application. Even if you’re sharing your UI with Compose Multiplatform,
  you need this entry point for your iOS app. This is also where you should add SwiftUI code for your project.

* [/shared](./shared/src) is for code that will be shared across your Compose Multiplatform applications.
  It contains several subfolders:
  - [commonMain](./shared/src/commonMain/kotlin) is for code that’s common for all targets.
  - Other folders are for Kotlin code that will be compiled for only the platform indicated in the folder name.
    For example, if you want to use Apple’s CoreCrypto for the iOS part of your Kotlin app,
    the [iosMain](./shared/src/iosMain/kotlin) folder would be the right place for such calls.
    Similarly, if you want to edit the Desktop (JVM) specific part, the [jvmMain](./shared/src/jvmMain/kotlin)
    folder is the appropriate location.

### Running the apps

Use the run configurations provided by the run widget in your IDE's toolbar. You can also use these commands and options:

- Android app: `./gradlew :androidApp:assembleDebug`
- iOS app: open the [/iosApp](./iosApp) directory in Xcode and run it from there.

### Running tests

Use the run button in your IDE's editor gutter, or run tests using Gradle tasks:

- Android tests: `./gradlew :shared:testAndroidHostTest`
- iOS tests: `./gradlew :shared:iosSimulatorArm64Test`

### Design

Figma: [Focus Box - Android App](https://www.figma.com/community/file/1684303802987760141) — the "Components" page (`node-id=16-290`) is the source of truth for colors, typography, and the shared component set; "Screens - Light & Dark" holds the full app flows.

Miro: [High-level architecture board](https://miro.com/app/board/uXjVHjlqnOY=/?share_link_id=154476372700) — screen flow across the 5 app sections, component architecture (packages, `TimerEngine`, services), and the SQLDelight data model.

### Theming and components

All shared UI lives in `commonMain` under `com.rizz.focusbox.ui`:

- **`ui.theme`** — `FocusBoxTheme { content() }` wraps a screen in the app's Material 3 color scheme and typography. It reads light/dark from `ThemeRepository` (persisted via `multiplatform-settings`) and resolves `System`/`Light`/`Dark` through `resolveDarkTheme()`.
  - `FocusBoxPalette.kt` — the light/dark `ColorScheme`s, mapped from Figma's hex tokens via `colorFromHex()`.
  - `FocusBoxTypography.kt` — the Roboto type scale from Figma's "Typography - Roboto" foundation, wired into `FocusBoxTheme`'s `MaterialTheme`. The one style with no Material 3 role (the 76sp running-timer digits) lives separately as `focusBoxDisplayTimerStyle()`.
  - Roboto is bundled as `.ttf` files under `shared/src/commonMain/composeResources/font/` (Regular + Medium) so the app looks the same on iOS, which has no Roboto pre-installed, rather than falling back to the OS default font.
- **`ui.components`** — one file per component (`FocusBoxButton.kt`, `StatCard.kt`, `ProgressRing.kt`, ...), each themed through `FocusBoxTheme` and each with `@Preview` functions for both light and dark. Standard Material 3 widgets (button, chip, switch, radio, text field, snackbar, top app bar, segmented control) are thin wrappers over Compose Material3's own composables; app-specific ones (progress ring, stat card, session item, settings row, navigation bar, stepper) are built to match the Figma component set directly.

To add a new component: create one file in `ui/components`, wrap its content in `FocusBoxTheme(darkTheme = ...)` for both `@Preview` variants, and pull colors/text styles from `MaterialTheme.colorScheme`/`MaterialTheme.typography` (never hardcode a color or font size) so it stays in sync with theme changes automatically.

---

Learn more about [Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html)…