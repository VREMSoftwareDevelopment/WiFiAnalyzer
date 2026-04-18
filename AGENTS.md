# AI Assistant Instructions for WiFiAnalyzer

## Project Overview

WiFiAnalyzer is an Android application for analyzing WiFi networks. It helps users:
- Identify nearby Access Points
- Graph channel signal strength
- Analyze Wi-Fi networks to rate channels
- Support 2.4 GHz, 5 GHz and 6 GHz Wi-Fi bands
- Export access point details

**Important**: WiFiAnalyzer is NOT a Wi-Fi password cracking or phishing tool.

## Technology Stack

| Component | Technology |
|-----------|------------|
| Language | Kotlin |
| Platform | Android |
| Build Tool | Gradle |
| Testing | JUnit, Mockito, Robolectric, Espresso |
| Code Style | ktlint |
| License | GNU General Public License v3.0 (GPLv3) |

clearAdditional repository-specific versions and toolchain (source-of-truth files shown):

- Kotlin: 2.3.20 (top-level `build.gradle` ext.kotlin_version)
- Android Gradle Plugin (AGP): 9.1.1 (top-level `build.gradle` classpath `com.android.tools.build:gradle:9.1.1`)
 - Note: the top-level `build.gradle` also adds `gradlePluginPortal()` to repositories and includes additional classpath entries used by the build:
   - `org.jetbrains.kotlin:kotlin-allopen:$kotlin_version`
   - `com.github.ben-manes:gradle-versions-plugin:0.53.0`
- Gradle wrapper: 9.4.1 (`gradle/wrapper/gradle-wrapper.properties` distributionUrl)
- JDK: 21 is used in CI and repository setup (`.github/actions/common-setup/action.yml` and `.github/workflows/*` use setup-java with `java-version: 21`). Note: project `compileOptions` and `kotlinOptions.jvmTarget` are set to Java 17 in `app/build.gradle`.
- Android compile/target SDK: compileSdk = 36, minSdk = 24 (see `app/build.gradle`).

## Project Structure

```
app/src/main/kotlin/         # Main application source code
app/src/test/kotlin/         # Unit tests
app/src/androidTest/kotlin/  # Android instrumentation tests
```

## Coding Standards

### File Headers

All source files must include the GPLv3 license header:

```kotlin
/*
 * WiFiAnalyzer
 * Copyright (C) 2015 - {current_year} VREM Software Development <VREMSoftwareDevelopment@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */
```

### Naming Conventions

- Use descriptive names for classes, methods, and variables
- Follow Kotlin naming conventions: camelCase for variables/methods, PascalCase for classes

### Code Formatting

Use ktlint for code formatting:
- Check: `./gradlew ktlintCheck`
- Format: `./gradlew ktlintFormat`
 
Repository-specific ktlint notes:
- Plugin configured in `app/build.gradle` as `org.jlleitschuh.gradle.ktlint` (version `14.2.0`).
- Baseline and rules: see `app/config/ktlint/baseline.xml` and project `.editorconfig` for formatting rules.

## Testing Requirements

### Mandatory Testing

All new features and bug fixes MUST include unit tests.

### Test File Naming

Test files use several patterns, including but not limited to:
- `[ClassName]Test.kt`
- `[ClassName]InstrumentedTest.kt`
- `[ClassName]IntegrationTest.kt`
- `[ClassName]ParameterizedTest.kt`
- `[ClassName]TestUtil.kt`

Use the pattern that best describes the test's purpose. Document any deviations from these patterns in your code or pull request to maintain clarity.

### Test Structure (AAA Pattern)

```kotlin
@Test
fun shouldReturnCorrectVersionNumber() {
    // Arrange: Set up test data and mocks
    // Act: Execute the code being tested
    // Assert: Verify the results
}
```

### Testing Patterns Used

// Example imports for test files:
```kotlin
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
```

// Example imports for assertions:
```kotlin
import org.assertj.core.api.Assertions.assertThat

assertThat(actual).isEqualTo(expected)
assertThat(actual).isTrue
assertThat(actual).isNotNull()
```

**Test teardown pattern:**
```kotlin
@After
fun tearDown() {
    verifyNoMoreInteractions(dependency1, dependency2)
}
```

**Robolectric for Android components (use RobolectricUtil helper):**
```kotlin
import com.vrem.wifianalyzer.RobolectricUtil

private val mainActivity = RobolectricUtil.INSTANCE.activity

// For fragments:
RobolectricUtil.INSTANCE.startFragment(fragment)
```

## Android Instrumentation Test Conventions

- Instrumentation test files are located in `app/src/androidTest/kotlin/`.
- File names typically follow the pattern `[ClassName]InstrumentedTest.kt`.
- Use the `@RunWith(AndroidJUnit4::class)` annotation for instrumentation tests.
- Access UI components using Espresso or Robolectric as appropriate.
- Example instrumentation test structure:

```kotlin
@RunWith(AndroidJUnit4::class)
class MainActivityInstrumentedTest {
    @Test
    fun shouldDisplayMainScreen() {
        // Arrange: Launch activity
        // Act: Interact with UI
        // Assert: Verify UI state
    }
}
```

## Build Commands

| Task | Command |
|------|---------|
| Check code style | `./gradlew ktlintCheck` |
| Format code | `./gradlew ktlintFormat` |
| Run lint | `./gradlew lintDebug` |
| Run unit tests | `./gradlew testDebugUnitTest` |
| Run tests with coverage | `./gradlew jacocoTestCoverageVerification` |
| Run instrumented tests | `./gradlew connectedDebugAndroidTest` |

### CI / GitHub Actions (what the repo runs)

- Workflows:
  - `.github/workflows/android-ci.yml` — main Android CI pipeline (jobs: ktlint, lint, test, coverage, build-apk, emulator-test). Runners use `ubuntu-24.04` / `ubuntu-latest` and a composite action `.github/actions/common-setup` to install JDK 21 and Gradle.
  - `.github/workflows/codeql-analysis.yml` — CodeQL analysis (language: `java-kotlin`, uses JDK 21).

- Important CI details and artifact/report locations (useful for reproducing or debugging locally):
  - ktlint report: `app/build/reports/ktlint` (CI uploads as `ktlint-report`).
  - lint report: `app/build/reports/lint-results*.*` (CI uploads as `lint-report`).
  - unit test reports: `app/build/reports/tests` (CI uploads as `test-results`). The unit test task invoked is `:app:testDebugUnitTest` / `./gradlew testDebugUnitTest`.
  - JaCoCo report (CI expects the XML): `app/build/reports/jacoco/jacocoTestReport/jacocoTestReport.xml` (uploaded to Codecov using `secrets.CODECOV_TOKEN`).
  - APK artifact: `app/build/outputs/apk/debug` (uploaded as `artifact-apk`).
  - Instrumentation / emulator test outputs: `app/build/reports/androidTests` and `app/build/outputs/androidTest-results/connected/**/*.xml` for JUnit XMLs.

- Emulator job notes: the GitHub Action enables KVM, caches AVD (`~/.android/avd/*`) and runs `./gradlew connectedDebugAndroidTest`. Emulator caching and KVM are required for the `emulator-test` job in `android-ci.yml`.

## Privacy and Security Guidelines

1. **No Data Collection**: WiFiAnalyzer does not collect any personal/device information
2. **No Internet**: The app does not require internet access
3. **Minimal Permissions**: Use only necessary Android permissions
4. **No Secrets**: Never commit API keys, passwords, or other secrets
